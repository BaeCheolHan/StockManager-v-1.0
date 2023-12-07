package com.my.stock.stockmanager.dto.stock.response;

import com.my.stock.stockmanager.rdb.entity.Stock;
import com.my.stock.stockmanager.redis.entity.DividendInfo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

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
}
