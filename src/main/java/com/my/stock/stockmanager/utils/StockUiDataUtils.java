package com.my.stock.stockmanager.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StockUiDataUtils {

	public String getOverSeaCompareToYesterdaySign(BigDecimal compareToYesterday) {
		String sign;
		if (compareToYesterday.compareTo(BigDecimal.ZERO) > 0) {
			sign = "1";
		} else if (compareToYesterday.compareTo(BigDecimal.ZERO) == 0) {
			sign = "3";
		} else {
			sign = "5";
		}

		return sign;
	}
}
