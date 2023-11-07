package com.my.stock.stockmanager.dto.stock.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyDetailStockInfoResponse extends BaseResponse {

	MyDetailStockInfo detail;

	@Builder
	public MyDetailStockInfoResponse(MyDetailStockInfo detail, ResponseCode code, String message) {
		super(code, message);
		this.detail = detail;
	}
}
