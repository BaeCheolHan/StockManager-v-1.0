package com.my.stock.stockmanager.dto.stock.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailStockChartSeriesResponse extends BaseResponse {

	List<DetailStockChartSeries> chartData;

	@Builder
	public DetailStockChartSeriesResponse(List<DetailStockChartSeries> chartData, ResponseCode code, String message) {
		super(code, message);
		this.chartData = chartData;
	}
}
