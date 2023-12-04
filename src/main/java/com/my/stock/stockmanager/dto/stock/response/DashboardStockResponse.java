package com.my.stock.stockmanager.dto.stock.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stock.DashboardStock;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DashboardStockResponse extends BaseResponse {
	final List<DashboardStock> stocks;

	@Builder
	public DashboardStockResponse(List<DashboardStock> stocks, ResponseCode code, String message) {
		super(code, message);
		this.stocks = stocks;
	}
}
