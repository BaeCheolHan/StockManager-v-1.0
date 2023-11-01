package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KrDailyStockChartPriceOutput1 {
	// 전일 대비
	private String prdy_vrss;
	// 전일 대비 부호
	private String prdy_vrss_sign;
	// 전일 대비율
	private String prdy_ctrt;
	// 주식 전일 종가
	private String stck_prdy_clpr;
	// 누적 거래량
	private String acml_vol;
	// 누적 거래 대금
	private String acml_tr_pbmn;
	// HTS 한글 종목명
	private String hts_kor_isnm;
	// 주식 현재가
	private String stck_prpr;
	// 주식 단축 종목코드
	private String stck_shrn_iscd;
	// 전일 거래량
	private String prdy_vol;
	// 상한가
	private String stck_mxpr;
	// 하한가
	private String stck_llam;
	// 시가
	private String stck_oprc;
	// 최고가
	private String stck_hgpr;
	// 최저가
	private String stck_lwpr;
	// 주식 전일 시가
	private String stck_prdy_oprc;
	// 주식 전일 최고가
	private String stck_prdy_hgpr;
	// 주식 전일 최저가
	private String stck_prdy_lwpr;
	// 매도호가
	private String askp;
	// 매수호가
	private String bidp;
	// 전일 대비 거래량
	private String prdy_vrss_vol;
	// 거래량 회전율
	private String vol_tnrt;
	// 주식 액면가
	private String stck_fcam;
	// 상장 주수
	private String lstn_stcn;
	// 자본금
	private String cpfn;
	// HTS 시가총액
	private String hts_avls;
	private String per;
	private String eps;
	private String pbr;
	// 전체 융자 잔고 비율
	private String itewhol_loan_rmnd_ratem;
}
