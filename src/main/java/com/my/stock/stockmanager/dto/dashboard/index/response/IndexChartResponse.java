package com.my.stock.stockmanager.dto.dashboard.index.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.kis.response.KrDailyIndexChartPriceWrapper;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyIndexChartPriceWrapper;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexChartResponse extends BaseResponse {
	private KrDailyIndexChartPriceWrapper kospi;
	private KrDailyIndexChartPriceWrapper kosdaq;

	private OverSeaDailyIndexChartPriceWrapper snp;
	private OverSeaDailyIndexChartPriceWrapper nasdaq;
	private OverSeaDailyIndexChartPriceWrapper daw;
	private OverSeaDailyIndexChartPriceWrapper philadelphia;


	@Builder
	public IndexChartResponse(ResponseCode code, String message, KrDailyIndexChartPriceWrapper kospi, KrDailyIndexChartPriceWrapper kosdaq, OverSeaDailyIndexChartPriceWrapper snp, OverSeaDailyIndexChartPriceWrapper nasdaq, OverSeaDailyIndexChartPriceWrapper daw, OverSeaDailyIndexChartPriceWrapper philadelphia) {
		super(code, message);
		this.kospi = kospi;
		this.kosdaq = kosdaq;
		this.snp = snp;
		this.nasdaq = nasdaq;
		this.daw = daw;
		this.philadelphia = philadelphia;
	}

}
