package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KrDailyStockChartPriceOutput2 {
	// 주식 영업 일자
	private String stck_bsop_date;
	// 주식 종가
	private String stck_clpr;
	// 주식 시가
	private String stck_oprc;
	// 주식 최고가
	private String stck_hgpr;
	// 주식 최저가
	private String stck_lwpr;
	// 누적 거래량
	private String acml_vol;
	// 누적 거래 대금
	private String acml_tr_pbmn;

	/**
	 * 락 구분 코드
	 * 00:해당사항없음(락이 발생안한 경우)
	 * 01:권리락
	 * 02:배당락
	 * 03:분배락
	 * 04:권배락
	 * 05:중간(분기)배당락
	 * 06:권리중간배당락
	 * 07:권리분기배당락
	 */
	private String flng_cls_code;

	// 분할 비율
	private String prtt_rate;
	// 분할변경여부 Y, N
	private String mod_yn;
	// 전일 대비 부호
	private String prdy_vrss_sign;
	// 전일 대비
	private String prdy_vrss;
	// 재평가사유코드
	private String revl_issu_reas;
}
