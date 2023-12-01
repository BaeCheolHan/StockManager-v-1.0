package com.my.stock.stockmanager.dto.dividend.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.dividend.DividendInfo;
import com.my.stock.stockmanager.dto.dividend.DividendInfoByItem;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DividendInfoByItemResponse extends BaseResponse {
	List<DividendInfoByItem> data;

	@Builder
	public DividendInfoByItemResponse(List<DividendInfoByItem> data, ResponseCode code, String message) {
		super(code, message);
		this.data = data;
	}
}
