package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KrDailyIndexChartPriceWrapper {
	private String rt_cd;
	private String msg_cd;
	private String msg1;
	private KrDailyIndexChartPriceOutput1 output1;
	private List<KrDailyIndexChartPriceOutput2> output2;
}
