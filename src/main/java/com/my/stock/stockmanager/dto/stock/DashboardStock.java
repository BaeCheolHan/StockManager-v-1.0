package com.my.stock.stockmanager.dto.stock;

import lombok.*;

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
	private Double avgPrice;
	private Double quantity;
}
