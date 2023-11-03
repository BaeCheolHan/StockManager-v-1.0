package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//	조회 기간별 시세
public class KrDailyIndexChartPriceOutput2 {
	// 누적 거래 대금
	private String acml_tr_pbmn;
	// 누적 거래량
	private String acml_vol;
	// 업종 지수 최저가
	private String bstp_nmix_lwpr;
	// 업종 지수 최고가
	private String bstp_nmix_hgpr;
	// 업종 지수 시가
	private String bstp_nmix_oprc;
	// 업종 지수 현재가
	private String bstp_nmix_prpr;
	// 영업 일자
	private String stck_bsop_date;
	// 변경 여부
	private String mod_yn;
}
