package com.my.stock.stockmanager.dto.stock.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stock.DashboardStock;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailStockInfoResponse extends BaseResponse {

	DetailStockInfo detail;

	@Builder
	public DetailStockInfoResponse(DetailStockInfo detail, ResponseCode code, String message) {
		super(code, message);
		this.detail = detail;
	}
}
