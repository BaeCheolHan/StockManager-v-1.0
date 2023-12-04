package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.api.kis.KisApi;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.kis.request.KrDailyStockChartPriceRequest;
import com.my.stock.stockmanager.dto.kis.request.OverSeaDailyStockChartPriceRequest;
import com.my.stock.stockmanager.dto.kis.response.KrDailyStockChartPriceOutput2;
import com.my.stock.stockmanager.dto.kis.response.KrDailyStockChartPriceWrapper;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyStockChartPriceOutput2;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyStockChartPriceWrapper;
import com.my.stock.stockmanager.dto.stock.DashboardStock;
import com.my.stock.stockmanager.dto.stock.request.StockSaveRequest;
import com.my.stock.stockmanager.dto.stock.response.DetailStockChartSeries;
import com.my.stock.stockmanager.dto.stock.response.DetailStockInfoResponse;
import com.my.stock.stockmanager.dto.stock.response.MyDetailStockInfo;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.mongodb.documents.MyStockList;
import com.my.stock.stockmanager.mongodb.repository.MyStockListRepository;
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
import com.my.stock.stockmanager.utils.KisApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

	private final StockRepository stockRepository;
	private final ExchangeRateRepository exchangeRateRepository;

	private final MemberRepository memberRepository;
	private final DividendRepository dividendRepository;

	private final BankAccountDataService bankAccountDataService;
	private final StocksDataService stocksDataService;
	private final OverSeaNowStockPriceDataService overSeaNowStockPriceDataService;
	private final KrNowStockPriceDataService krNowStockPriceDataService;

	private final KisApi kisApi;

	private final KisApiUtils kisApiUtils;

	private final MyStockListRepository myStockListRepository;

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

		List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
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

				String sign;
				if (compareToYesterday.compareTo(BigDecimal.ZERO) > 0) {
					sign = "1";
				} else if (compareToYesterday.compareTo(BigDecimal.ZERO) == 0) {
					sign = "3";
				} else {
					sign = "5";
				}

				stock.setCompareToYesterdaySign(sign);
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

		// 특정 계좌 주식 내역 mongo Insert
		updateMyStockList(account.getMember().getId(), account.getId());
		// 전체 계좌 주식 내역 mongo Insert
		updateMyStockList(account.getMember().getId(), null);
	}

	public void updateMyStockList(String memberId, Long bankId) {
		String mongoId = bankId == null ? memberId.toString() : String.format("%s%s", memberId, bankId);
		MyStockList myStocks = myStockListRepository.findById(mongoId).orElse(null);

		if (myStocks == null || myStocks.getData().isEmpty()) {
			myStocks = new MyStockList();
		}

		List<DashboardStock> data = stockRepository.findAllDashboardStock(memberId, bankId);
		myStocks.setData(data);
		myStocks.setId(mongoId);
		myStockListRepository.save(myStocks);
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
	public MyDetailStockInfo getMyDetailStock(String memberId, String national, String code, String symbol) throws Exception {

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
					.chartData(this.getKrDailyChart("D", symbol))
					.build();
		} else {
			OverSeaNowStockPrice entity = overSeaNowStockPriceDataService.findById(symbol);

			BigDecimal base = entity.getBase();
			BigDecimal last = entity.getLast();
			BigDecimal compareToYesterday = last.subtract(base);

			String sign;
			if (compareToYesterday.compareTo(BigDecimal.ZERO) > 0) {
				sign = "1";
			} else if (compareToYesterday.compareTo(BigDecimal.ZERO) == 0) {
				sign = "3";
			} else {
				sign = "5";
			}

			return MyDetailStockInfo.builder()
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
					.chartData(this.getOverSeaDailyChart("D", symbol))
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

	public List<DetailStockChartSeries> getDailyChartData(String chartType, String national, String symbol) throws Exception {
		if (national.equals("KR")) {
			return this.getKrDailyChart(chartType, symbol);
		} else {
			return this.getOverSeaDailyChart(chartType, symbol);
		}
	}

	public List<DetailStockChartSeries> getKrDailyChart(String chartType, String symbol) throws Exception {
		HttpHeaders headers = kisApiUtils.getDefaultApiHeader("FHKST03010100");
		headers.add("custtype", "P");

		KrDailyStockChartPriceRequest request = KrDailyStockChartPriceRequest.builder()
				.FID_COND_MRKT_DIV_CODE("J")
				.FID_INPUT_ISCD(symbol)
				.FID_INPUT_DATE_1(LocalDate.now().minusYears(100L).minusDays(1L).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.FID_INPUT_DATE_2(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.FID_PERIOD_DIV_CODE(chartType)
				.FID_ORG_ADJ_PRC("0")
				.build();
		KrDailyStockChartPriceWrapper resp = kisApi.getKrDailyStockChartPrice(headers, request);

		final DateTimeFormatter dfm = DateTimeFormatter.ofPattern("yyyyMMdd");
		List<KrDailyStockChartPriceOutput2> output2 = resp.getOutput2().stream().filter(it -> it.getStck_bsop_date() != null).sorted(Comparator.comparing(s -> LocalDate.parse(s.getStck_bsop_date(), dfm))).toList();
		resp.setOutput2(output2);

		return resp.getOutput2().stream()
				.filter(it -> it.getStck_bsop_date() != null).map(it -> {
					DetailStockChartSeries series = new DetailStockChartSeries();
					series.setOpen(it.getStck_oprc());
					series.setHigh(it.getStck_hgpr());
					series.setLow(it.getStck_lwpr());
					series.setClose(it.getStck_clpr());
					series.setDate(it.getStck_bsop_date());
					return series;
				}).collect(Collectors.toList());
	}


	public List<DetailStockChartSeries> getOverSeaDailyChart(String chartType, String symbol) throws Exception {
		HttpHeaders headers = kisApiUtils.getDefaultApiHeader("HHDFS76240000");
		headers.add("custtype", "P");
		Stocks stock = stocksDataService.findBySymbol(symbol);

		String gubnValue = "0";

		if (chartType.equals("D")) {
			gubnValue = "0";
		}
		if (chartType.equals("W")) {
			gubnValue = "1";
		}
		if (chartType.equals("M")) {
			gubnValue = "2";
		}
		OverSeaDailyStockChartPriceRequest request = OverSeaDailyStockChartPriceRequest.builder()
				.AUTH("")
				.EXCD(stock.getCode())
				.SYMB(stock.getSymbol())
				.GUBN(gubnValue)
				.BYMD(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.MODP("1")
				.build();

		OverSeaDailyStockChartPriceWrapper resp = kisApi.getOverSeaDailyStockChartPrice(headers, request);

		Collections.reverse(resp.getOutput2());

		final DateTimeFormatter dfm = DateTimeFormatter.ofPattern("yyyyMMdd");
		List<OverSeaDailyStockChartPriceOutput2> output2 = resp.getOutput2().stream().filter(it -> it.getXymd() != null).sorted(Comparator.comparing(s -> LocalDate.parse(s.getXymd(), dfm))).toList();
		resp.setOutput2(output2);

		return resp.getOutput2().stream().filter(it -> it.getXymd() != null).map(it -> {
			DetailStockChartSeries series = new DetailStockChartSeries();
			series.setDate(it.getXymd());
			series.setOpen(it.getOpen());
			series.setHigh(it.getHigh());
			series.setLow(it.getLow());
			series.setClose(it.getClos());
			return series;
		}).collect(Collectors.toList());
	}

	public DetailStockInfoResponse getDetailStock(String symbol) throws Exception {
		KrNowStockPrice detail = krNowStockPriceDataService.findById(symbol);
		return DetailStockInfoResponse.builder()
				.code(ResponseCode.SUCCESS)
				.message(ResponseCode.SUCCESS.getMessage())
				.detail(detail)
				.chartData(this.getKrDailyChart("D", symbol))
				.build();
	}
}
