package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KrDailyStockChartPriceWrapper {
	private String rt_cd;
	private String msg_cd;
	private String msg1;

	private KrDailyStockChartPriceOutput1 output1;
	private List<KrDailyStockChartPriceOutput2> output2;
}
