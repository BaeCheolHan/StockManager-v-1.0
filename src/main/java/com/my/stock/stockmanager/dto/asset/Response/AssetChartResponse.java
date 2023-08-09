package com.my.stock.stockmanager.dto.asset.Response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.asset.AssetChart;
import com.my.stock.stockmanager.dto.stock.DashboardStock;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AssetChartResponse extends BaseResponse {
	AssetChart assetCharts;

	@Builder
	public AssetChartResponse(ResponseCode code, String message, AssetChart assetCharts) {
		super(code, message);
		this.assetCharts = assetCharts;
	}
}
