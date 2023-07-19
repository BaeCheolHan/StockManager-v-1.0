package com.my.stock.stockmanager.dto.dividend;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DividendInfo {
	private Long id;
	private int year;
	private int month;
	private int day;
	private String symbol;
	private BigDecimal dividend;
	private String code;
	private String name;
	private String national;
}
