package com.my.stock.stockmanager.dto.stock;

import lombok.*;

import java.math.BigDecimal;
import com.my.stock.stockmanager.model.Money;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStock {
	private String symbol;
	private String code;
	private String national;
	private String name;
	private BigDecimal avgPrice;
	private BigDecimal quantity;
	private BigDecimal totalDividend;
	private BigDecimal priceImportance;
	// 주식 현재가
	private BigDecimal nowPrice;

	private String compareToYesterdaySign;
	private BigDecimal compareToYesterday;

	// 수익률
	private String rateOfReturnPer;

	public void calculateRateOfReturn() {
		if (nowPrice == null || avgPrice == null) {
			this.rateOfReturnPer = "0.00";
			return;
		}
		BigDecimal change = nowPrice.subtract(avgPrice);
		this.rateOfReturnPer = Money.percentToString(change, avgPrice);
	}

	public void calculatePriceImportance(BigDecimal totalInvestment, BigDecimal exchangeRateBasePriceIfOversea) {
		BigDecimal baseAmount = nowPrice;
		if (baseAmount == null || quantity == null) {
			this.priceImportance = BigDecimal.ZERO;
			return;
		}
		BigDecimal amount = baseAmount.multiply(quantity);
		if (!"KR".equals(national) && exchangeRateBasePriceIfOversea != null) {
			amount = amount.multiply(exchangeRateBasePriceIfOversea);
		}
		this.priceImportance = Money.percent(amount, totalInvestment);
	}
}
