package com.my.stock.stockmanager.dto.dividend.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.dividend.DividendChart;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DividendChartResponse extends BaseResponse {
	final List<DividendChart> series;

	@Builder
	public DividendChartResponse(List<DividendChart> series, ResponseCode code, String message) {
		super(code, message);
		this.series = series;
	}
}
