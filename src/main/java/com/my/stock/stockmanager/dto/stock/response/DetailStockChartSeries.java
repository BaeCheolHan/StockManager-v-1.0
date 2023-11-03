package com.my.stock.stockmanager.dto.stock.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailStockChartSeries {
	private String date;
	private String open;
	private String high;
	private String low;
	private String close;
}
