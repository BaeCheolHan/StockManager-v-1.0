package com.my.stock.stockmanager.redis.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@RedisHash("KrNowStockPrice")
public class KrNowStockPrice {
	// 주식 단축 종목코드
	@Id
	private String symbol;

	private String stck_shrn_iscd;

	// 주식 현재가
	private BigDecimal stck_prpr;

	//    종목 상태 구분 코드
//    00 : 그외
//    51 : 관리종목
//    52 : 투자의견
//    53 : 투자경고
//    54 : 투자주의
//    55 : 신용가능
//    57 : 증거금 100%
//    58 : 거래정지
//    59 : 단기과열,
	private String iscd_stat_cls_code;

	// 증거금 비율
	private BigDecimal marg_rate;
	// 대표 시장 한글 명
	private String rprs_mrkt_kor_name;

	// 업종 한글 종목명
	private String bstp_kor_isnm;

	// 임시 정지 여부
	private String temp_stop_yn;

	// 시가 범위 연장 여부
	private String oprc_rang_cont_yn;

	// 종가 범위 연장 여부
	private String clpr_rang_cont_yn;

	// 신용 가능 여부
	private String crdt_able_yn;

	// 보증금 비율 구분 코드
	//	한국투자 증거금비율 (marg_rate 참고)
	//	40 : 20%, 30%, 40%
	//	50 : 50%
	//	60 : 60%
	private String grmn_rate_cls_code;

	// ELW 발행 여부
	private String elw_pblc_yn;

	// 전일 대비
	private BigDecimal prdy_vrss;

	//전일 대비 부호
	//	1 : 상한
	//	2 : 상승
	//	3 : 보합
	//	4 : 하한
	//	5 : 하락
	private String prdy_vrss_sign;

	// 전일 대비율
	private BigDecimal prdy_ctrt;

	// 누적 거래 대금
	private Long acml_tr_pbmn;

	// 누적 거래량
	private Long acml_vol;

	// 전일 대비 거래량 비율
	private BigDecimal prdy_vrss_vol_rate;
	// 주식 시가
	private BigDecimal stck_oprc;
	// 주식 최고가
	private BigDecimal stck_hgpr;
	// 주식 최저가
	private BigDecimal stck_lwpr;
	// 주식 상한가
	private BigDecimal stck_mxpr;
	// 주식 하한가
	private BigDecimal stck_llam;
	// 주식 기준가
	private BigDecimal stck_sdpr;
	// 가중 평균 주식 가격
	private BigDecimal wghn_avrg_stck_prc;
	// HTS 외국인 소진율
	private BigDecimal hts_frgn_ehrt;
	// 외국인 순매수 수량
	private Long frgn_ntby_qty;
	// 프로그램매매 순매수 수량
	private Long pgtr_ntby_qty;
	// 피벗 2차 디저항 가격(직원용 데이터)
	private Long pvt_scnd_dmrs_prc;
	// 피벗 1차 디저항 가격(직원용 데이터)
	private Long pvt_frst_dmrs_prc;
	// 피벗 포인트 값(직원용 데이터)
	private Long pvt_pont_val;
	// 피벗 1차 디지지 가격(직원용 데이터)
	private Long pvt_frst_dmsp_prc;
	// 피벗 2차 디지지 가격(직원용 데이터)
	private Long pvt_scnd_dmsp_prc;
	// 디저항 값(직원용 데이터)
	private Long dmrs_val;
	// 디지지 값(직원용 데이터)
	private Long dmsp_val;
	// 자본금
	private Long cpfn;
	// 제한 폭 가격
	private Long rstc_wdth_prc;
	// 주식 액면가
	private Integer stck_fcam;
	// 주식 대용가
	private Integer stck_sspr;
	// 호가단위
	private Integer aspr_unit;
	// HTS 매매 수량 단위 값
	private BigDecimal hts_deal_qty_unit_val;
	// 상장 주수
	private Long lstn_stcn;
	// HTS 시가총액
	private Long hts_avls;
	// PER
	private BigDecimal per;
	// PBR
	private BigDecimal pbr;
	// 결산 월
	private String stac_month;
	// 거래량 회전율
	private BigDecimal vol_tnrt;
	// EPS
	private BigDecimal eps;
	// BPS
	private BigDecimal bps;
	// 250일 최고가
	private BigDecimal d250_hgpr;
	// 250일 최고가 일자
	private String d250_hgpr_date;
	// 250일 최고가 대비 현재가 비율
	private BigDecimal d250_hgpr_vrss_prpr_rate;
	// 250일 최저가
	private BigDecimal d250_lwpr;
	// 250일 최저가 일자
	private String d250_lwpr_date;
	// 250일 최저가 대비 현재가 비율
	private BigDecimal d250_lwpr_vrss_prpr_rate;
	// 주식 연중 최고가
	private BigDecimal stck_dryy_hgpr;
	// 연중 최고가 대비 현재가 비율
	private BigDecimal dryy_hgpr_vrss_prpr_rate;
	// 연중 최고가 일자
	private String dryy_hgpr_date;
	// 주식 연중 최저가
	private BigDecimal stck_dryy_lwpr;
	// 	연중 최저가 대비 현재가 비율
	private BigDecimal dryy_lwpr_vrss_prpr_rate;
	// 연중 최저가 일자
	private String dryy_lwpr_date;
	// 52주일 최고가
	private BigDecimal w52_hgpr;
	// 52주일 최고가 대비 현재가 대비
	private BigDecimal w52_hgpr_vrss_prpr_ctrt;
	// 52주일 최고가 일자
	private String w52_hgpr_date;
	// 52주일 최저가
	private BigDecimal w52_lwpr;
	// 52주일 최저가 대비 현재가 대비
	private BigDecimal w52_lwpr_vrss_prpr_ctrt;
	// 52주일 최저가 일자
	private String w52_lwpr_date;
	// 전체 융자 잔고 비율
	private BigDecimal whol_loan_rmnd_rate;
	// 공매도가능여부
	private String ssts_yn;
	// 액면가 통화명
	private String fcam_cnnm;
	// 자본금 통화명(외국주권은 억으로 떨어지며, 그 외에는 만으로 표시됨)
	private String cpfn_cnnm;
	// 외국인 보유 수량
	private Long frgn_hldn_qty;
	// VI적용구분코드
	private String vi_cls_code;
	// 시간외단일가VI적용구분코드
	private String ovtm_vi_cls_code;
	// 최종 공매도 체결 수량
	private Long last_ssts_cntg_qty;
	// 투자유의여부
	private String invt_caful_yn;
	// 시장경고코드
	//		00 : 없음
	//		01 : 투자주의
	//		02 : 투자경고
	//		03 : 투자위험
	private String mrkt_warn_cls_code;
	// 단기과열여부
	private String short_over_yn;
	// ??
	private String sltr_yn;
	// 신 고가 저가 구분 코드	조회하는 종목이 신고/신저에 도달했을 경우에만 조회됨
	private String new_hgpr_lwpr_cls_code;

	private DividendInfo dividendInfo;

	@TimeToLive(unit = TimeUnit.MINUTES)
	@Builder.Default
	private Long expiration = 5L;
}
