package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverSeaDailyIndexChartPriceOutput1 {
	// 전일 대비
	private String ovrs_nmix_prdy_vrss;
	// 전일 대비 부호
	private String prdy_vrss_sign;
	// 전일 대비율
	private String prdy_ctrt;
	// 전일 종가
	private String ovrs_nmix_prdy_clpr;
	// 누적 거래량
	private String acml_vol;
	// HTS 한글 종목명
	private String hts_kor_isnm;
	// 현재가
	private String ovrs_nmix_prpr;
	// 단축 종목코드
	private String stck_shrn_iscd;
	// 시가
	private String ovrs_prod_oprc;
	// 최고가
	private String ovrs_prod_hgpr;
	// 최저가
	private String ovrs_prod_lwpr;

}
