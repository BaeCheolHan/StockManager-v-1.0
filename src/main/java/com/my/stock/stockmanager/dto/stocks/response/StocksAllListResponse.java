package com.my.stock.stockmanager.dto.stocks.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.rdb.entity.Stocks;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class StocksAllListResponse extends BaseResponse {
	final List<Stocks> stocks;

	@Builder
	public StocksAllListResponse(List<Stocks> stocks, ResponseCode code, String message) {
		super(code, message);
		this.stocks = stocks;
	}
}
