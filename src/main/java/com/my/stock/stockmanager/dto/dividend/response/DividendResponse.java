package com.my.stock.stockmanager.dto.dividend.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.rdb.entity.Dividend;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DividendResponse extends BaseResponse {
	final List<Dividend> data;

	@Builder
	public DividendResponse(List<Dividend> data, ResponseCode code, String message) {
		super(code, message);
		this.data = data;
	}
}
