package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stock.DashboardStock;
import com.my.stock.stockmanager.dto.stock.request.StockSaveRequest;
import com.my.stock.stockmanager.dto.stock.response.DetailStockInfo;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.data.service.BankAccountDataService;
import com.my.stock.stockmanager.rdb.data.service.StocksDataService;
import com.my.stock.stockmanager.rdb.entity.*;
import com.my.stock.stockmanager.rdb.repository.DividendRepository;
import com.my.stock.stockmanager.rdb.repository.ExchangeRateRepository;
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
import com.my.stock.stockmanager.rdb.repository.StockRepository;
import com.my.stock.stockmanager.redis.data.service.KrNowStockPriceDataService;
import com.my.stock.stockmanager.redis.data.service.OverSeaNowStockPriceDataService;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import com.my.stock.stockmanager.redis.entity.OverSeaNowStockPrice;
import com.my.stock.stockmanager.redis.repository.KrNowStockPriceRepository;
import com.my.stock.stockmanager.redis.repository.OverSeaNowStockPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

	private final StockRepository stockRepository;
	private final ExchangeRateRepository exchangeRateRepository;
	private final KrNowStockPriceRepository krNowStockPriceRepository;
	private final OverSeaNowStockPriceRepository overSeaNowStockPriceRepository;
	private final MemberRepository memberRepository;
	private final DividendRepository dividendRepository;


	private final BankAccountDataService bankAccountDataService;
	private final StocksDataService stocksDataService;
	private final OverSeaNowStockPriceDataService overSeaNowStockPriceDataService;
	private final KrNowStockPriceDataService krNowStockPriceDataService;


	public List<DashboardStock> getStocks(Long memberId, Long bankId) {
		List<DashboardStock> stocks = stockRepository.findAllDashboardStock(memberId, bankId);
		List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
		stocks.forEach(stock -> {
			if (!stock.getNational().equals("KR")) {
				Optional<OverSeaNowStockPrice> entity = overSeaNowStockPriceRepository.findById(stock.getSymbol());
				entity.ifPresent(it -> {
					stock.setNowPrice(it.getLast());
					BigDecimal nowPrice = it.getLast();
					BigDecimal avgPrice = stock.getAvgPrice();

					stock.setRateOfReturnPer(nowPrice.subtract(avgPrice).divide(avgPrice, 4, RoundingMode.DOWN)
							.multiply(BigDecimal.valueOf(100).setScale(2, RoundingMode.FLOOR)).toString());
				});
			} else {
				Optional<KrNowStockPrice> entity = krNowStockPriceRepository.findById(stock.getSymbol());
				entity.ifPresent(it -> {
					stock.setNowPrice(it.getStck_prpr());
					BigDecimal nowPrice = it.getStck_prpr();
					BigDecimal avgPrice = stock.getAvgPrice();
					stock.setRateOfReturnPer(nowPrice.subtract(avgPrice).divide(avgPrice, 4, RoundingMode.DOWN)
							.multiply(BigDecimal.valueOf(100).setScale(2, RoundingMode.FLOOR)).toString());
				});

			}
		});

		final BigDecimal totalInvestmentAmount = stocks.stream()
				.map(it -> {
					if (it.getNowPrice() == null) {
						try {
							BigDecimal nowPrice = this.setNowPriceAndGetNowPrice(it.getSymbol());
							it.setNowPrice(nowPrice);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
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
			setNowPriceAndGetNowPrice(request.getSymbol());
		}

	}

	private BigDecimal setNowPriceAndGetNowPrice(String symbol) throws Exception {
		Stocks stocks = stocksDataService.findBySymbol(symbol);
		if (!stocks.getNational().equals("KR")) {
			return overSeaNowStockPriceDataService.findById(symbol).getLast();
		} else {
			return krNowStockPriceDataService.findById(symbol).getStck_prpr();
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
			OverSeaNowStockPrice entity = overSeaNowStockPriceDataService.findById(symbol);

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
