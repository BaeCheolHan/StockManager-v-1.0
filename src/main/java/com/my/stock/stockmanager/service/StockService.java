package com.my.stock.stockmanager.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.constants.National;
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
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
import com.my.stock.stockmanager.rdb.repository.MyStockSnapShotRepository;
import com.my.stock.stockmanager.rdb.repository.StockRepository;
import com.my.stock.stockmanager.redis.data.service.NowStockPriceDataService;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import com.my.stock.stockmanager.redis.entity.OverSeaNowStockPrice;
import com.my.stock.stockmanager.utils.StockUiDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {
	private final NowStockPriceDataService nowStockPriceDataService;
	private final StockRepository stockRepository;
	private final MemberRepository memberRepository;
	private final DividendRepository dividendRepository;
	private final BankAccountDataService bankAccountDataService;
	private final StocksDataService stocksDataService;
	private final MyStockSnapShotRepository myStockSnapShotRepository;
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
			if (!National.from(stock.getNational()).isKr()) {
				OverSeaNowStockPrice overSeaNowStockPrice = nowStockPriceDataService.findOverSeaById(stock.getSymbol());
				stock.setNowPrice(overSeaNowStockPrice.getLast());
				stock.calculateRateOfReturn();
				BigDecimal compareToYesterday = overSeaNowStockPrice.compareToYesterday();
				stock.setCompareToYesterdaySign(stockUiDataUtils.getOverSeaCompareToYesterdaySign(compareToYesterday));
				stock.setCompareToYesterday(compareToYesterday);
			} else {
				KrNowStockPrice krNowStockPrice = nowStockPriceDataService.findKrById(stock.getSymbol());
				stock.setNowPrice(krNowStockPrice.getStck_prpr());
				stock.calculateRateOfReturn();
				stock.setCompareToYesterdaySign(stockUiDataUtils.getKrCompareToYesterdaySign(krNowStockPrice));
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
					if (!National.from(it.getNational()).isKr()) {
						return it.getNowPrice().multiply(exchangeRate.getBasePrice()).multiply(it.getQuantity());
					} else {
						return it.getNowPrice().multiply(it.getQuantity());
					}
				}).reduce(BigDecimal.ZERO, BigDecimal::add);
		stocks.forEach(stock -> {
			BigDecimal fx = National.from(stock.getNational()).isKr() ? null : exchangeRate.getBasePrice();
			stock.calculatePriceImportance(totalInvestmentAmount, fx);
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
		updateMyStockList(account.getMember().getId(), account.getId());
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
		if (!stocks.isKorean()) {
			return nowStockPriceDataService.findOverSeaById(symbol).getLast();
		} else {
			return nowStockPriceDataService.findKrById(symbol).getStck_prpr();
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
		if (National.from(national).isKr()) {
			KrNowStockPrice entity = nowStockPriceDataService.findKrById(symbol);
			return MyDetailStockInfo.ofKr(
					entity,
					totalDividend,
					stocks,
					chartService.getDailyChartData("D", national, symbol)
			);
		} else {
			OverSeaNowStockPrice entity = nowStockPriceDataService.findOverSeaById(symbol);
			return MyDetailStockInfo.ofOversea(
					entity,
					totalDividend,
					stocks,
					chartService.getDailyChartData("D", national, symbol)
			);
		}
	}

	@Transactional
	public void deleteById(Long id) throws StockManagerException {
		Stock stock = stockRepository.findById(id).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
		BankAccount account = stock.getBankAccount();
		stockRepository.deleteById(id);
		updateMyStockList(account.getMember().getId(), account.getId());
		updateMyStockList(account.getMember().getId(), null);
	}

	public MyDetailStockInfo getDetailStock(String symbol) throws Exception {
		Stocks stocks = stocksDataService.findBySymbol(symbol);
		if (stocks.isKorean()) {
			KrNowStockPrice entity = nowStockPriceDataService.findKrById(symbol);
			return MyDetailStockInfo.ofKr(
					entity,
					null,
					null,
					chartService.getDailyChartData("D", stocks.getNational(), symbol)
			);
		} else {
			OverSeaNowStockPrice entity = nowStockPriceDataService.findOverSeaById(symbol);
			return MyDetailStockInfo.ofOversea(
					entity,
					null,
					null,
					chartService.getDailyChartData("D", stocks.getNational(), symbol)
			);
		}
	}
}
