package com.my.stock.stockmanager.dto.stock.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StockSaveRequest {
	private Long bankId;
	private String symbol;
	private BigDecimal quantity;
	private BigDecimal price;
}
