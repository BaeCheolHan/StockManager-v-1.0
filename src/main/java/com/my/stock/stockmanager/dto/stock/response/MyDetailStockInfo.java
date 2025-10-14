package com.my.stock.stockmanager.dto.stock.response;

import java.math.BigDecimal;
import java.util.List;

import com.my.stock.stockmanager.rdb.entity.Stock;
import com.my.stock.stockmanager.redis.entity.DividendInfo;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import com.my.stock.stockmanager.redis.entity.OverSeaNowStockPrice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyDetailStockInfo {
	private BigDecimal startPrice;
	private BigDecimal highPrice;
	private BigDecimal lowPrice;
	private BigDecimal nowPrice;
	private BigDecimal totalDividend;
	private BigDecimal compareToYesterday;
	private String compareToYesterdaySign;
	private BigDecimal per;
	private BigDecimal pbr;
	private BigDecimal eps;
	private BigDecimal bps;
	// 배당수익율
	private BigDecimal cashrate;
	// 배당금
	private BigDecimal cashsis;
	private List<Stock> stocks;
	private List<DetailStockChartSeries> chartData;

	private DividendInfo dividendInfo;

	public static MyDetailStockInfo ofKr(KrNowStockPrice entity,
				BigDecimal totalDividend,
				List<Stock> stocks,
				List<DetailStockChartSeries> chartData) {
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
				.chartData(chartData)
				.dividendInfo(entity.getDividendInfo())
				.build();
	}

	public static MyDetailStockInfo ofOversea(OverSeaNowStockPrice entity,
				BigDecimal totalDividend,
				List<Stock> stocks,
				List<DetailStockChartSeries> chartData) {
		BigDecimal compareToYesterday = entity.compareToYesterday();
		String sign;
		int c = compareToYesterday == null ? 0 : compareToYesterday.compareTo(BigDecimal.ZERO);
		if (c > 0) sign = "1"; else if (c == 0) sign = "3"; else sign = "5";
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
				.chartData(chartData)
				.dividendInfo(entity.getDividendInfo())
				.build();
	}
}
