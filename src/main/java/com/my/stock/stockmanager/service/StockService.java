package com.my.stock.stockmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.kis.RestKisToken;
import com.my.stock.stockmanager.dto.stock.DashboardStock;
import com.my.stock.stockmanager.dto.stock.KrNowStockPriceWrapper;
import com.my.stock.stockmanager.dto.stock.OverSeaNowStockPriceWrapper;
import com.my.stock.stockmanager.dto.stock.request.StockSaveRequest;
import com.my.stock.stockmanager.dto.stock.response.DetailStockInfo;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.global.infra.ApiCaller;
import com.my.stock.stockmanager.rdb.entity.*;
import com.my.stock.stockmanager.rdb.repository.*;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import com.my.stock.stockmanager.redis.entity.OverSeaNowStockPrice;
import com.my.stock.stockmanager.redis.repository.KrNowStockPriceRepository;
import com.my.stock.stockmanager.redis.repository.OverSeaNowStockPriceRepository;
import com.my.stock.stockmanager.utils.KisTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {
	@Value("${api.kis.appKey}")
	private String appKey;
	@Value("${api.kis.app-secret}")
	private String appSecret;

	private final StockRepository stockRepository;

	private final ExchangeRateRepository exchangeRateRepository;

	private final BankAccountRepository bankAccountRepository;

	private final KrNowStockPriceRepository krNowStockPriceRepository;

	private final OverSeaNowStockPriceRepository overSeaNowStockPriceRepository;

	private final MemberRepository memberRepository;

	private final StocksRepository stocksRepository;

	private final KisTokenProvider kisTokenProvider;

	public List<DashboardStock> getStocks(Long memberId, Long bankId) {
		List<DashboardStock> stocks = stockRepository.findAllDashboardStock(memberId, bankId);
		List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();

		final double totalInvestmentAmount = stocks.stream()
				.mapToDouble(it -> {
					if (!it.getNational().equals("KR")) {
						return it.getAvgPrice() * exchangeRateList.get(exchangeRateList.size() - 1).getBasePrice() * it.getQuantity();
					} else {
						return it.getAvgPrice() * it.getQuantity();
					}
				}).sum();

		stocks.forEach(stock -> {
			if (!stock.getNational().equals("KR")) {
				stock.setPriceImportance((stock.getAvgPrice() * exchangeRateList.get(exchangeRateList.size() - 1).getBasePrice() * stock.getQuantity()) / totalInvestmentAmount * 100.0);
				Optional<OverSeaNowStockPrice> entity = overSeaNowStockPriceRepository.findById("D".concat(stock.getCode()).concat(stock.getSymbol()));
				entity.ifPresent(it -> {
					stock.setNowPrice(it.getLast());
					BigDecimal nowPrice1 = BigDecimal.valueOf(it.getLast());
					BigDecimal avgPrice = BigDecimal.valueOf(stock.getAvgPrice());

					stock.setRateOfReturnPer(nowPrice1.subtract(avgPrice).divide(avgPrice, 4, RoundingMode.DOWN)
							.multiply(BigDecimal.valueOf(100)).toString());
				});
			} else {
				stock.setPriceImportance((stock.getAvgPrice() * stock.getQuantity()) / totalInvestmentAmount * 100.0);
				Optional<KrNowStockPrice> entity = krNowStockPriceRepository.findById(stock.getSymbol());
				entity.ifPresent(it -> {
					stock.setNowPrice(it.getStck_prpr());
					BigDecimal nowPrice1 = BigDecimal.valueOf(it.getStck_prpr());
					BigDecimal avgPrice = BigDecimal.valueOf(stock.getAvgPrice());
					stock.setRateOfReturnPer(nowPrice1.subtract(avgPrice).divide(avgPrice, 4, RoundingMode.DOWN)
							.multiply(BigDecimal.valueOf(100)).toString());
				});
			}
		});
		return stocks.stream().sorted(Comparator.comparing(DashboardStock::getPriceImportance).reversed()).collect(Collectors.toList());
	}

	@Async("asyncTaskExecutor")
	public void requestBatchRun() {
		try {
			ApiCaller.getInstance().get("http://127.0.0.1:18082/batch/run/NowKrStockPriceGettingJob", new HashMap<>());
			ApiCaller.getInstance().get("http://127.0.0.1:18082/batch/run/ToNightOverSeaStockPriceGettingJob", new HashMap<>());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public void saveStock(StockSaveRequest request) throws Exception {
		BankAccount account = bankAccountRepository.findById(request.getBankId())
				.orElseThrow(() -> new StockManagerException("존재하지 않는 은행 식별키입니다.", ResponseCode.NOT_FOUND_ID));

		Optional<Stock> existedStock = stockRepository.findFirstBySymbol(request.getSymbol());

		Stock stock = new Stock();
		stock.setBankAccount(account);
		stock.setPrice(request.getPrice());
		stock.setSymbol(request.getSymbol());
		stock.setQuantity(request.getQuantity());
		stockRepository.save(stock);

		if (existedStock.isEmpty()) {
			setNowPrice(request);
		}

	}

	private void setNowPrice(StockSaveRequest request) throws Exception {
		Stocks stocks = stocksRepository.findBySymbol(request.getSymbol())
				.orElseThrow(() -> new StockManagerException("존재하지 않는 은행 식별키입니다.", ResponseCode.NOT_FOUND_ID));

		if (!stocks.getNational().equals("KR")) {
			RestKisToken kisToken = kisTokenProvider.getRestToken();
			HttpHeaders headers = new HttpHeaders();
			headers.add("authorization", kisToken.getToken_type() + " " + kisToken.getAccess_token());
			headers.add("content-type", "application/json; charset=utf-8");
			headers.add("appkey", appKey);
			headers.add("appsecret", appSecret);
			headers.add("tr_id", "HHDFS76200200");
			headers.add("custtype", "P");

			HashMap<String, Object> param = new HashMap<>();
			param.put("AUTH", "");
			param.put("EXCD", stocks.getCode());
			param.put("SYMB", stocks.getSymbol());
			OverSeaNowStockPriceWrapper response = new ObjectMapper().readValue(ApiCaller.getInstance()
							.get("https://openapi.koreainvestment.com:9443/uapi/overseas-price/v1/quotations/price-detail", headers, param)
					, OverSeaNowStockPriceWrapper.class);

			Optional<OverSeaNowStockPrice> entity = overSeaNowStockPriceRepository.findById(response.getOutput().getRsym());
			entity.ifPresent(overSeaNowStockPriceRepository::delete);
			overSeaNowStockPriceRepository.save(response.getOutput());
		} else {
			RestKisToken kisToken = kisTokenProvider.getRestToken();
			HttpHeaders headers = new HttpHeaders();
			headers.add("authorization", kisToken.getToken_type() + " " + kisToken.getAccess_token());
			headers.add("content-type", "application/json; charset=utf-8");
			headers.add("appkey", appKey);
			headers.add("appsecret", appSecret);
			headers.add("tr_id", "FHKST01010100");

			HashMap<String, Object> param = new HashMap<>();
			param.put("FID_COND_MRKT_DIV_CODE", "J");
			param.put("FID_INPUT_ISCD", stocks.getSymbol());
			KrNowStockPriceWrapper response = new ObjectMapper().readValue(ApiCaller.getInstance()
							.get("https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/quotations/inquire-price", headers, param)
					, KrNowStockPriceWrapper.class);
			Optional<KrNowStockPrice> entity = krNowStockPriceRepository.findById(response.getOutput().getStck_shrn_iscd());
			entity.ifPresent(krNowStockPriceRepository::delete);
			krNowStockPriceRepository.save(response.getOutput());
		}
	}

	@Transactional
	public DetailStockInfo getDetail(Long memberId, String national, String code, String symbol) {

		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));

		List<Stock> stocks = new ArrayList<>();
		member.getBankAccount()
				.forEach(bank -> stocks.addAll(bank.getStocks().stream().filter(stock -> stock.getSymbol().equals(symbol)).toList()));

		if (national.equals("KR")) {
			KrNowStockPrice entity = krNowStockPriceRepository.findById(symbol)
					.orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));

			String sign = "";
			if (Integer.parseInt(entity.getPrdy_vrss_sign()) < 3) {
				sign = "plus";
			} else if (Integer.parseInt(entity.getPrdy_vrss_sign()) == 3) {
				sign = "done";
			} else {
				sign = "minus";
			}

			return DetailStockInfo.builder()
					.compareToYesterday(entity.getPrdy_vrss())
					.compareToYesterdaySign(sign)
					.startPrice(entity.getStck_oprc())
					.nowPrice(entity.getStck_prpr())
					.highPrice(entity.getStck_hgpr())
					.lowPrice(entity.getStck_lwpr())
					.pbr(entity.getPbr())
					.per(entity.getPer())
					.eps(entity.getEps())
					.bps(entity.getEps())
					.stocks(stocks)
					.build();
		} else {
			OverSeaNowStockPrice entity = overSeaNowStockPriceRepository.findById("D".concat(code).concat(symbol))
					.orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));

			BigDecimal base = BigDecimal.valueOf(entity.getBase());
			BigDecimal last = BigDecimal.valueOf(entity.getLast());
			Double compareToYesterday = Double.valueOf(base.subtract(last).toString());

			String sign = "";
			if (compareToYesterday > 0) {
				sign = "plus";
			} else if (compareToYesterday == 0) {
				sign = "done";
			} else {
				sign = "minus";
			}

			return DetailStockInfo.builder()
					.compareToYesterday(compareToYesterday)
					.compareToYesterdaySign(sign)
					.startPrice(entity.getOpen())
					.nowPrice(entity.getLast())
					.highPrice(entity.getHigh())
					.lowPrice(entity.getLow())
					.pbr(entity.getPbrx())
					.per(entity.getPerx())
					.eps(entity.getEpsx())
					.bps(entity.getEpsx())
					.stocks(stocks)
					.build();
		}
	}

	public void deleteById(Long id) {
		stockRepository.deleteById(id);
	}
}
