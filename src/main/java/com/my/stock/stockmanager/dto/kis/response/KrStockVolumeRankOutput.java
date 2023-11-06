package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KrStockVolumeRankOutput {
	// HTS 한글 종목명
	private String hts_kor_isnm;
	// 	유가증권 단축 종목코드
	private String mksc_shrn_iscd;
	// 데이터 순위
	private String data_rank;
	// 주식 현재가
	private String stck_prpr;
	// 전일 대비 부호
	private String prdy_vrss_sign;
	// 전일 대비
	private String prdy_vrss;
	// 전일 대비율
	private String prdy_ctrt;
	// 누적 거래량
	private String acml_vol;
	// 전일 거래량
	private String prdy_vol;
	// 상장 주수
	private String lstn_stcn;
	// 평균 거래량
	private String avrg_vol;
	// N일전종가대비현재가대비율
	private String n_befr_clpr_vrss_prpr_rate;
	// 거래량증가율
	private String vol_inrt;
	// 거래량 회전율
	private String vol_tnrt;
	// N일 거래량 회전율
	private String nday_vol_tnrt;
	// 평균 거래 대금
	private String avrg_tr_pbmn;
	// 거래대금회전율
	private String tr_pbmn_tnrt;
	// 	N일 거래대금 회전율
	private String nday_tr_pbmn_tnrt;
	// 누적 거래 대금
	private String acml_tr_pbmn;
}
