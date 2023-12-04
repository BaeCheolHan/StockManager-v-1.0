package com.my.stock.stockmanager.dto.asset.Response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.asset.AssetChart;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AssetChartResponse extends BaseResponse {
	final AssetChart assetCharts;

	@Builder
	public AssetChartResponse(ResponseCode code, String message, AssetChart assetCharts) {
		super(code, message);
		this.assetCharts = assetCharts;
	}
}
