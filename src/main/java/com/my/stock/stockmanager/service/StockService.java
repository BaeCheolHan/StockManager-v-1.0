package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stock.DashboardStock;
import com.my.stock.stockmanager.dto.stock.request.StockSaveRequest;
import com.my.stock.stockmanager.dto.stock.response.MyDetailStockInfo;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.mongodb.documents.MyStockList;
import com.my.stock.stockmanager.mongodb.repository.MyStockListRepository;
import com.my.stock.stockmanager.rdb.data.service.BankAccountDataService;
import com.my.stock.stockmanager.rdb.data.service.StocksDataService;
import com.my.stock.stockmanager.rdb.entity.BankAccount;
import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import com.my.stock.stockmanager.rdb.entity.Member;
import com.my.stock.stockmanager.rdb.entity.MyStockSnapShot;
import com.my.stock.stockmanager.rdb.entity.Stock;
import com.my.stock.stockmanager.rdb.entity.Stocks;
import com.my.stock.stockmanager.rdb.repository.DividendRepository;
import com.my.stock.stockmanager.rdb.repository.ExchangeRateRepository;
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
import com.my.stock.stockmanager.rdb.repository.MyStockSnapShotRepository;
import com.my.stock.stockmanager.rdb.repository.StockRepository;
import com.my.stock.stockmanager.redis.data.service.KrNowStockPriceDataService;
import com.my.stock.stockmanager.redis.data.service.OverSeaNowStockPriceDataService;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import com.my.stock.stockmanager.redis.entity.OverSeaNowStockPrice;
import com.my.stock.stockmanager.utils.StockUiDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {
	// redis
	private final ExchangeRateRepository exchangeRateRepository;
	private final OverSeaNowStockPriceDataService overSeaNowStockPriceDataService;
	private final KrNowStockPriceDataService krNowStockPriceDataService;
	// rdb
	private final StockRepository stockRepository;
	private final MemberRepository memberRepository;
	private final DividendRepository dividendRepository;
	private final BankAccountDataService bankAccountDataService;
	private final StocksDataService stocksDataService;
	private final MyStockSnapShotRepository myStockSnapShotRepository;

	// mongodb
	private final MyStockListRepository myStockListRepository;

	private final StockUiDataUtils stockUiDataUtils;

	private final ChartService chartService;
	private final ExchangeRateService exchangeRateService;

	public List<DashboardStock> getStocks(String memberId, Long bankId) {

		String id = String.format("%s%s", memberId, bankId == null ? "" : bankId.toString());
		MyStockList myStocks = myStockListRepository.findById(id).orElse(null);

		if (myStocks == null || myStocks.getData().isEmpty()) {
			myStocks = new MyStockList();
			List<DashboardStock> data = stockRepository.findAllDashboardStock(memberId, bankId);
			myStocks.setData(data);
			myStocks.setId(id);
			myStockListRepository.save(myStocks);
		}

		List<DashboardStock> stocks = myStocks.getData();


		ExchangeRate exchangeRate = exchangeRateService.getExchangeRate();
		stocks.forEach(stock -> {
			if (!stock.getNational().equals("KR")) {
				OverSeaNowStockPrice overSeaNowStockPrice = overSeaNowStockPriceDataService.findById(stock.getSymbol());
				stock.setNowPrice(overSeaNowStockPrice.getLast());
				BigDecimal nowPrice = overSeaNowStockPrice.getLast();
				BigDecimal avgPrice = stock.getAvgPrice();

				stock.setRateOfReturnPer(nowPrice.subtract(avgPrice).divide(avgPrice, 4, RoundingMode.DOWN)
						.multiply(BigDecimal.valueOf(100).setScale(2, RoundingMode.FLOOR)).toString());

				BigDecimal base = overSeaNowStockPrice.getBase();
				BigDecimal last = overSeaNowStockPrice.getLast();
				BigDecimal compareToYesterday = last.subtract(base);

				stock.setCompareToYesterdaySign(stockUiDataUtils.getOverSeaCompareToYesterdaySign(compareToYesterday));
				stock.setCompareToYesterday(compareToYesterday);
			} else {
				KrNowStockPrice krNowStockPrice = krNowStockPriceDataService.findById(stock.getSymbol());

				stock.setNowPrice(krNowStockPrice.getStck_prpr());
				BigDecimal nowPrice = krNowStockPrice.getStck_prpr();
				BigDecimal avgPrice = stock.getAvgPrice();
				stock.setRateOfReturnPer(nowPrice.subtract(avgPrice).divide(avgPrice, 4, RoundingMode.DOWN)
						.multiply(BigDecimal.valueOf(100).setScale(2, RoundingMode.FLOOR)).toString());

				stock.setCompareToYesterdaySign(krNowStockPrice.getPrdy_vrss_sign());
				stock.setCompareToYesterday(krNowStockPrice.getPrdy_vrss());

			}
		});

		final BigDecimal totalInvestmentAmount = stocks.stream()
				.map(it -> {
					if (it.getNowPrice() == null) {
						try {
							BigDecimal nowPrice = this.getStockNowPrice(it.getSymbol());
							it.setNowPrice(nowPrice);
						} catch (Exception e) {
							it.setNowPrice(BigDecimal.ZERO);
						}
					}
					if (!it.getNational().equals("KR")) {
						return it.getNowPrice().multiply(exchangeRate.getBasePrice()).multiply(it.getQuantity());
					} else {
						return it.getNowPrice().multiply(it.getQuantity());
					}
				}).reduce(BigDecimal.ZERO, BigDecimal::add);

		stocks.forEach(stock -> {
			if (!stock.getNational().equals("KR")) {
				stock.setPriceImportance(
						stock.getNowPrice()
								.multiply(exchangeRate.getBasePrice())
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
		BankAccount account = bankAccountDataService.findByIdJoinFetch(request.getBankId());

		Optional<Stock> existedStock = stockRepository.findFirstBySymbol(request.getSymbol());

		Stock stock = new Stock();
		stock.setBankAccount(account);
		stock.setPrice(request.getPrice());
		stock.setSymbol(request.getSymbol());
		stock.setQuantity(request.getQuantity());
		stockRepository.save(stock);

		Optional<MyStockSnapShot> mySnapShot = account.getMyStockSnapShot().stream()
				.filter(snapShot -> snapShot.getSymbol().equals(request.getSymbol()))
				.findFirst();


		mySnapShot.ifPresentOrElse(myStockSnapShot -> {
			// 주식 존재하는 경우 스냅샷
		}, () -> {
			// 주식 없는경우 스냅샷
			MyStockSnapShot snapShot = new MyStockSnapShot();
			snapShot.setSymbol(request.getSymbol());
			snapShot.setQuantity(request.getQuantity());
			snapShot.setAverPrice(request.getPrice());
			snapShot.setBankAccount(account);
			myStockSnapShotRepository.save(snapShot);
		});

		if (existedStock.isEmpty()) {
			this.getStockNowPrice(request.getSymbol());
		}

		// 특정 계좌 주식 내역 mongo Insert
		updateMyStockList(account.getMember().getId(), account.getId());
		// 전체 계좌 주식 내역 mongo Insert
		updateMyStockList(account.getMember().getId(), null);
	}

	public void updateMyStockList(String memberId, Long bankId) {
		String mongoId = bankId == null ? memberId : String.format("%s%s", memberId, bankId);
		MyStockList myStocks = myStockListRepository.findById(mongoId).orElse(null);

		if (myStocks == null || myStocks.getData().isEmpty()) {
			myStocks = new MyStockList();
		}

		List<DashboardStock> data = stockRepository.findAllDashboardStock(memberId, bankId);
		myStocks.setData(data);
		myStocks.setId(mongoId);
		myStockListRepository.save(myStocks);
	}

	private BigDecimal getStockNowPrice(String symbol) throws StockManagerException {
		Stocks stocks = stocksDataService.findBySymbol(symbol);
		if (!stocks.getNational().equals("KR")) {
			return overSeaNowStockPriceDataService.findById(symbol).getLast();
		} else {
			return krNowStockPriceDataService.findById(symbol).getStck_prpr();
		}
	}

	@Transactional
	public MyDetailStockInfo getMyDetailStock(String memberId, String national, String symbol) throws Exception {

		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));

		List<Stock> stocks = new ArrayList<>();
		member.getBankAccount()
				.forEach(bank -> stocks.addAll(bank.getStocks().stream().filter(stock -> stock.getSymbol().equals(symbol)).toList()));

		BigDecimal totalDividend = dividendRepository.findDividendSumByMemberIdAndSymbol(memberId, symbol);

		if (national.equals("KR")) {
			KrNowStockPrice entity = krNowStockPriceDataService.findById(symbol);

			return MyDetailStockInfo.builder()
					.compareToYesterday(entity.getPrdy_vrss())
					.compareToYesterdaySign(entity.getPrdy_vrss_sign())
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
					.chartData(chartService.getDailyChartData("D", national, symbol))
					.dividendInfo(entity.getDividendInfo())
					.build();
		} else {
			OverSeaNowStockPrice entity = overSeaNowStockPriceDataService.findById(symbol);

			BigDecimal base = entity.getBase();
			BigDecimal last = entity.getLast();
			BigDecimal compareToYesterday = last.subtract(base);

			return MyDetailStockInfo.builder()
					.compareToYesterday(compareToYesterday)
					.compareToYesterdaySign(stockUiDataUtils.getOverSeaCompareToYesterdaySign(compareToYesterday))
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
					.chartData(chartService.getDailyChartData("D", national, symbol))
					.dividendInfo(entity.getDividendInfo())
					.build();
		}
	}

	@Transactional
	public void deleteById(Long id) throws StockManagerException {
		Stock stock = stockRepository.findById(id).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
		BankAccount account = stock.getBankAccount();
		stockRepository.deleteById(id);

		// 특정 계좌 주식 내역 mongo Insert
		updateMyStockList(account.getMember().getId(), account.getId());
		// 전체 계좌 주식 내역 mongo Insert
		updateMyStockList(account.getMember().getId(), null);
	}

	public MyDetailStockInfo getDetailStock(String symbol) throws Exception {
		Stocks stocks = stocksDataService.findBySymbol(symbol);
		if (stocks.getNational().equals("KR")) {
			KrNowStockPrice entity = krNowStockPriceDataService.findById(symbol);

			return MyDetailStockInfo.builder()
					.compareToYesterday(entity.getPrdy_vrss())
					.compareToYesterdaySign(entity.getPrdy_vrss_sign())
					.startPrice(entity.getStck_oprc())
					.nowPrice(entity.getStck_prpr())
					.highPrice(entity.getStck_hgpr())
					.lowPrice(entity.getStck_lwpr())
					.pbr(entity.getPbr())
					.per(entity.getPer())
					.eps(entity.getEps())
					.bps(entity.getEps())
					.chartData(chartService.getDailyChartData("D", stocks.getNational(), symbol))
					.dividendInfo(entity.getDividendInfo())
					.build();
		} else {
			OverSeaNowStockPrice entity = overSeaNowStockPriceDataService.findById(symbol);

			BigDecimal base = entity.getBase();
			BigDecimal last = entity.getLast();
			BigDecimal compareToYesterday = last.subtract(base);

			return MyDetailStockInfo.builder()
					.compareToYesterday(compareToYesterday)
					.compareToYesterdaySign(stockUiDataUtils.getOverSeaCompareToYesterdaySign(compareToYesterday))
					.startPrice(entity.getOpen())
					.nowPrice(entity.getLast())
					.highPrice(entity.getHigh())
					.lowPrice(entity.getLow())
					.pbr(entity.getPbrx())
					.per(entity.getPerx())
					.eps(entity.getEpsx())
					.bps(entity.getEpsx())
					.chartData(chartService.getDailyChartData("D", stocks.getNational(), symbol))
					.dividendInfo(entity.getDividendInfo())
					.build();
		}

	}
}
