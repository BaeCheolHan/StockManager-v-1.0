package com.my.stock.stockmanager.dto.stock.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockSaveRequest {
	private Long bankId;
	private String symbol;
	private Long quantity;
	private Long price;
}
