package com.my.stock.stockmanager.dto.stock.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailStockInfoResponse extends BaseResponse {

	private final MyDetailStockInfo detail;

	private final List<DetailStockChartSeries> chartData;

	@Builder
	public DetailStockInfoResponse( ResponseCode code, String message, MyDetailStockInfo detail, List<DetailStockChartSeries> chartData) {
		super(code, message);
		this.detail = detail;
		this.chartData = chartData;
	}
}
