package com.my.stock.stockmanager.dto.dividend.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.dividend.DividendInfoDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DividendInfoResponse extends BaseResponse {
	final List<DividendInfoDto> data;

	@Builder
	public DividendInfoResponse(List<DividendInfoDto> data, ResponseCode code, String message) {
		super(code, message);
		this.data = data;
	}
}
