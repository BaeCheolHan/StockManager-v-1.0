package com.my.stock.stockmanager.dto.dashboard.volumerank.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.kis.response.KrDailyIndexChartPriceWrapper;
import com.my.stock.stockmanager.dto.kis.response.KrStockVolumeRankOutput;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyIndexChartPriceWrapper;
import com.my.stock.stockmanager.redis.entity.KrStockVolumeRank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KrStockVolumeRankResponse extends BaseResponse {

	List<KrStockVolumeRankOutput> data;


	@Builder
	public KrStockVolumeRankResponse(ResponseCode code, String message, List<KrStockVolumeRankOutput> data) {
		super(code, message);
		this.data = data;
	}

}
