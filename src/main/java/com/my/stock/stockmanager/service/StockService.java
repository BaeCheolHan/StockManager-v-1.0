package com.my.stock.stockmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stock.DashboardStock;
import com.my.stock.stockmanager.dto.stock.KrNowStockPriceWrapper;
import com.my.stock.stockmanager.dto.stock.OverSeaNowStockPriceWrapper;
import com.my.stock.stockmanager.dto.stock.request.StockSaveRequest;
import com.my.stock.stockmanager.dto.stock.response.DetailStockInfo;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.global.infra.ApiCaller;
import com.my.stock.stockmanager.rdb.data.service.BankAccountDataService;
import com.my.stock.stockmanager.rdb.data.service.StocksDataService;
import com.my.stock.stockmanager.rdb.entity.*;
import com.my.stock.stockmanager.rdb.repository.DividendRepository;
import com.my.stock.stockmanager.rdb.repository.ExchangeRateRepository;
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
import com.my.stock.stockmanager.rdb.repository.StockRepository;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import com.my.stock.stockmanager.redis.entity.OverSeaNowStockPrice;
import com.my.stock.stockmanager.redis.repository.KrNowStockPriceRepository;
import com.my.stock.stockmanager.redis.repository.OverSeaNowStockPriceRepository;
import com.my.stock.stockmanager.utils.KisApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

	private final StockRepository stockRepository;
	private final ExchangeRateRepository exchangeRateRepository;
	private final KrNowStockPriceRepository krNowStockPriceRepository;
	private final OverSeaNowStockPriceRepository overSeaNowStockPriceRepository;
	private final MemberRepository memberRepository;
	private final DividendRepository dividendRepository;

	private final KisApiUtils kisApiUtils;

	private final BankAccountDataService bankAccountDataService;
	private final StocksDataService stocksDataService;




	public List<DashboardStock> getStocks(Long memberId, Long bankId) {
		List<DashboardStock> stocks = stockRepository.findAllDashboardStock(memberId, bankId);
		List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
		stocks.forEach(stock -> {
			if (!stock.getNational().equals("KR")) {
				Optional<OverSeaNowStockPrice> entity = overSeaNowStockPriceRepository.findById("D".concat(stock.getCode()).concat(stock.getSymbol()));
				entity.ifPresent(it -> {
					stock.setNowPrice(it.getLast());
					BigDecimal nowPrice1 = it.getLast();
					BigDecimal avgPrice = stock.getAvgPrice();

					stock.setRateOfReturnPer(nowPrice1.subtract(avgPrice).divide(avgPrice, 4, RoundingMode.DOWN)
							.multiply(BigDecimal.valueOf(100).setScale(2, RoundingMode.FLOOR)).toString());
				});
			} else {
				Optional<KrNowStockPrice> entity = krNowStockPriceRepository.findById(stock.getSymbol());
				entity.ifPresent(it -> {
					stock.setNowPrice(it.getStck_prpr());
					BigDecimal nowPrice1 = it.getStck_prpr();
					BigDecimal avgPrice = stock.getAvgPrice();
					stock.setRateOfReturnPer(nowPrice1.subtract(avgPrice).divide(avgPrice, 4, RoundingMode.DOWN)
							.multiply(BigDecimal.valueOf(100).setScale(2, RoundingMode.FLOOR)).toString());
				});

			}
		});

		final BigDecimal totalInvestmentAmount = stocks.stream()
				.map(it -> {
					if (!it.getNational().equals("KR")) {
						return it.getNowPrice().multiply(exchangeRateList.get(exchangeRateList.size() - 1).getBasePrice()).multiply(it.getQuantity());
					} else {
						return it.getNowPrice().multiply(it.getQuantity());
					}
				}).reduce(BigDecimal.ZERO, BigDecimal::add);

		stocks.forEach(stock -> {
			if (!stock.getNational().equals("KR")) {
				stock.setPriceImportance(
						stock.getNowPrice()
								.multiply(exchangeRateList.get(exchangeRateList.size() - 1).getBasePrice())
								.multiply(stock.getQuantity())
								.divide(totalInvestmentAmount, RoundingMode.DOWN).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.FLOOR));

			} else {
				stock.setPriceImportance((stock.getNowPrice().multiply(stock.getQuantity())).divide(totalInvestmentAmount, RoundingMode.DOWN)
						.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.FLOOR));
			}
		});
		return stocks.stream().sorted(Comparator.comparing(DashboardStock::getPriceImportance).reversed()).collect(Collectors.toList());
	}

	@Transactional
	public void saveStock(StockSaveRequest request) throws Exception {
		BankAccount account = bankAccountDataService.findById(request.getBankId());

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
		Stocks stocks = stocksDataService.findBySymbol(request.getSymbol());

		HttpHeaders headers = kisApiUtils.getDefaultApiHeader();
		if (!stocks.getNational().equals("KR")) {
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
	public DetailStockInfo getDetail(Long memberId, String national, String code, String symbol) throws StockManagerException {

		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));

		List<Stock> stocks = new ArrayList<>();
		member.getBankAccount()
				.forEach(bank -> stocks.addAll(bank.getStocks().stream().filter(stock -> stock.getSymbol().equals(symbol)).toList()));

		BigDecimal totalDividend = dividendRepository.findDividendSumByMemberIdAndSymbol(memberId, symbol);

		if (national.equals("KR")) {
			KrNowStockPrice entity = krNowStockPriceRepository.findById(symbol)
					.orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));

			String sign;
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
					.totalDividend(totalDividend)
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

			BigDecimal base = entity.getBase();
			BigDecimal last = entity.getLast();
			BigDecimal compareToYesterday = base.subtract(last);

			String sign;
			if (compareToYesterday.compareTo(BigDecimal.ZERO) > 0) {
				sign = "plus";
			} else if (compareToYesterday.compareTo(BigDecimal.ZERO) == 0) {
				sign = "done";
			} else {
				sign = "minus";
			}

			return DetailStockInfo.builder()
					.compareToYesterday(compareToYesterday)
					.compareToYesterdaySign(sign)
					.totalDividend(totalDividend)
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
