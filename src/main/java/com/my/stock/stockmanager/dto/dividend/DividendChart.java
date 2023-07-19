package com.my.stock.stockmanager.dto.dividend;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class DividendChart {
	private String name;
	private List<BigDecimal> data;
	private BigDecimal total;
	private BigDecimal avg;
	private BigDecimal changeRate;

}
