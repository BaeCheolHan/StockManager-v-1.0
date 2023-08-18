package com.my.stock.stockmanager.dto.kis.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KrStockPriceRequest {
	private String fid_cond_mrkt_div_code;
	private String fid_input_iscd;
}
