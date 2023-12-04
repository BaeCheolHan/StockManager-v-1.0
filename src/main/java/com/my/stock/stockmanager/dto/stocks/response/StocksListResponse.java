package com.my.stock.stockmanager.dto.stocks.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.rdb.entity.Stocks;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class StocksListResponse extends BaseResponse {
	final List<Stocks> stocksList;

	@Builder
	public StocksListResponse(List<Stocks> stocksList, ResponseCode code, String message) {
		super(code, message);
		this.stocksList = stocksList;
	}
}
