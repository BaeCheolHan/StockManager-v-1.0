package com.my.stock.stockmanager.dto.stock;

import lombok.*;

import java.math.BigDecimal;

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
	private BigDecimal priceImportance;
	// 주식 현재가
	private BigDecimal nowPrice;

	private String rateOfReturnPer;
}
