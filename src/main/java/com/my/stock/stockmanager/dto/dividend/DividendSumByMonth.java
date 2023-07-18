package com.my.stock.stockmanager.dto.dividend;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DividendSumByMonth {
	private int year;
	private int month;
	private BigDecimal dividend;
}
