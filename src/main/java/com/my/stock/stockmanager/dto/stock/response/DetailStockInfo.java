package com.my.stock.stockmanager.dto.stock.response;

import com.my.stock.stockmanager.rdb.entity.Stock;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DetailStockInfo {
	private Double startPrice;
	private Double highPrice;
	private Double lowPrice;
	private Double nowPrice;
	private Double compareToYesterday;
	private String compareToYesterdaySign;
	private Double per;
	private Double pbr;
	private Double eps;
	private Double bps;
	private List<Stock> stocks;
}
