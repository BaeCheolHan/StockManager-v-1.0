package com.my.stock.stockmanager.utils;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import com.my.stock.stockmanager.redis.entity.OverSeaNowStockPrice;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;

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

	public String getOverSeaCompareToYesterdaySign(OverSeaNowStockPrice price) {
		return getOverSeaCompareToYesterdaySign(price.compareToYesterday());
	}

	public String getKrCompareToYesterdaySign(KrNowStockPrice price) {
		// 이미 KR은 prdy_vrss_sign을 제공하지만, 일관 API 제공
		return price.getPrdy_vrss_sign();
	}
}
