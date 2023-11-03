package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//	조회 기간별 시세
public class OverSeaDailyIndexChartPriceOutput2 {
	// 조회 일자
	private String stck_bsop_date;
	// 현재가
	private String ovrs_nmix_prpr;
	// 시작가
	private String ovrs_nmix_oprc;
	// 최고가
	private String ovrs_nmix_hgpr;
	// 최저가
	private String ovrs_nmix_lwpr;
	// 거래량
	private String acml_vol;
	private String mod_yn;
}
