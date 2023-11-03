package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KrDailyIndexChartPriceOutput1 {
	// 업종 전일 최저가
	private String futs_prdy_lwpr;
	// 업종 전일 최고가
	private String futs_prdy_hgpr;
	// 업종 전일 시가
	private String futs_prdy_oprc;
	// 업종 지수 최저가
	private String bstp_nmix_lwpr;
	// 업종 지수 최고가
	private String bstp_nmix_hgpr;
	// 업종 지수 시가
	private String bstp_nmix_oprc;
	// 전일 거래량
	private String prdy_vol;
	// 업종 구분 코드
	private String bstp_cls_code;
	// 업종 지수 현재가
	private String bstp_nmix_prpr;
	// HTS 한글 종목명
	private String hts_kor_isnm;
	// 누적 거래 대금
	private String acml_tr_pbmn;
	// 누적 거래량
	private String acml_vol;
	// 전일 지수
	private String prdy_nmix;
	// 업종 지수 전일 대비율
	private String bstp_nmix_prdy_ctrt;
	// 전일 대비 부호
	private String prdy_vrss_sign;
	// 업종 지수 전일 대비
	private String bstp_nmix_prdy_vrss;
}
