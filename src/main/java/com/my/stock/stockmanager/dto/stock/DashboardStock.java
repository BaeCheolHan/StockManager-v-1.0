package com.my.stock.stockmanager.dto.stock;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DashboardStock {
	private String symbol;
	private String code;
	private String national;
	private String name;
	private Double averPrice;
	private Long quantity;
}
