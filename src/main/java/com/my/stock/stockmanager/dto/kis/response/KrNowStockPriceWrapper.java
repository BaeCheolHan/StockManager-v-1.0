package com.my.stock.stockmanager.dto.kis.response;

import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KrNowStockPriceWrapper {
	private KrNowStockPrice output;
	private String rt_cd;
	private String msg_cd;
	private String msg1;
}
