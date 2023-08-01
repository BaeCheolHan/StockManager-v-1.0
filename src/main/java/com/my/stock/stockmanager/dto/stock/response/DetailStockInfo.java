package com.my.stock.stockmanager.dto.stock.response;

import com.my.stock.stockmanager.rdb.entity.Stock;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class DetailStockInfo {
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
	private List<Stock> stocks;
}
