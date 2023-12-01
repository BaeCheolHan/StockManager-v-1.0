package com.my.stock.stockmanager.dto.dividend;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DividendInfoByItem {
	private String code;
	private String name;
	private String symbol;
	private String national;
	private BigDecimal totalDividend;
}
