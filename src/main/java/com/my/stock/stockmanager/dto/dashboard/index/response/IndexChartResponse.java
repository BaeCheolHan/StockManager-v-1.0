package com.my.stock.stockmanager.dto.dashboard.index.response;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.kis.response.KrDailyIndexChartPriceWrapper;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyIndexChartPriceWrapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndexChartResponse extends BaseResponse {
	private KrDailyIndexChartPriceWrapper kospi;
	private KrDailyIndexChartPriceWrapper kosdaq;

	private OverSeaDailyIndexChartPriceWrapper snp;
	private OverSeaDailyIndexChartPriceWrapper nasdaq;


	@Builder
	public IndexChartResponse(ResponseCode code, String message, KrDailyIndexChartPriceWrapper kospi, KrDailyIndexChartPriceWrapper kosdaq, OverSeaDailyIndexChartPriceWrapper snp, OverSeaDailyIndexChartPriceWrapper nasdaq) {
		super(code, message);
		this.kospi = kospi;
		this.kosdaq = kosdaq;
		this.snp = snp;
		this.nasdaq = nasdaq;
	}

}
