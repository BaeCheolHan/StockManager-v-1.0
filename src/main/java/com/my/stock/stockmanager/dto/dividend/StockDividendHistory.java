package com.my.stock.stockmanager.dto.dividend;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDividendHistory {
	private String symbol;
	private String date;
	private BigDecimal dividend;
}
