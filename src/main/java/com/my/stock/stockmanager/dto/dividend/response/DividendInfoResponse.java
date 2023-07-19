package com.my.stock.stockmanager.dto.dividend.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.dividend.DividendChart;
import com.my.stock.stockmanager.dto.dividend.DividendInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DividendInfoResponse extends BaseResponse {
	List<DividendInfo> data;

	@Builder
	public DividendInfoResponse(List<DividendInfo> data, ResponseCode code, String message) {
		super(code, message);
		this.data = data;
	}
}
