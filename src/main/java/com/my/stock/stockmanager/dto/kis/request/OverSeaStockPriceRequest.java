package com.my.stock.stockmanager.dto.kis.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OverSeaStockPriceRequest {
	private String AUTH;
	private String EXCD;
	private String SYMB;
}
