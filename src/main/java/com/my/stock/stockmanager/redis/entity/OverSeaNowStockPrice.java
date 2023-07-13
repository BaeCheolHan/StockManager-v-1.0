package com.my.stock.stockmanager.redis.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash("OverSeaNowStockPrice")
public class OverSeaNowStockPrice {
	// 실시간조회종목코드
//	D+시장구분(3자리)+종목코드
//	예) DNASAAPL : D+NAS(나스닥)+AAPL(애플)
//[시장구분]
//	NYS : 뉴욕, NAS : 나스닥, AMS : 아멕스 ,
//	TSE : 도쿄, HKS : 홍콩,
//	SHS : 상해, SZS : 심천
//	HSX : 호치민, HNX : 하노이
	@Id
	private String rsym;
	// 소수점자리수
	private Integer zdiv;
	//통화
	private String curr;
	// 매매단위
	private Double vnit;
	// 시가(해당일 최초 거래가격)
	private Double open;
	// 고가(해당일 가장 높은 거래가격)
	private Double high;
	// 저가(해당일 가장 낮은 거래가격)
	private Double low;
	// 현재가
	private Double last;
	// 전일종가
	private Double base;
	// 전일 거래량
	private Long pvol;
	//전일거래대금
	private Double pamt;
	// 상한가
	private Double uplp;
	// 하한가
	private Double dnlp;
	// 52주최고가
	private Double h52p;
	// 52주최고일자
	private String h52d;
	// 52주최저가
	private Double l52p;
	// 52주최저일자
	private String l52d;
	// PER
	private Double perx;
	// PBR
	private Double pbrx;
	//EPS
	private Double epsx;
	//BPS
	private Double bpsx;

	// 상장주수
	private Long shar;
	// 자본금
	private Long mcap;
	// 시가총액
	private Long tomv;
	// 원환산당일가격
	private Double t_xprc;
	//원환산당일대비
	private Double t_xdif;
	// 원환산당일등락
	private Double t_xrat;
	// 원환산전일가격
	private Double p_xprc;
	// 원환산전일대비
	private Double p_xdif;
	// 원환산전일등락
	private Double p_xrat;
	// 당일환율
	private Double t_rate;
	// 전일환율
	private Double p_rate;
	// 원환산당일기호
	private String t_xsgn;
	// 원환산전일기호
	private String p_xsng;
	// 거래가능여부
	private String e_ordyn;
	// 호가단위
	private Double e_hogau;
	// 업종(섹터)
	private String e_icod;
	// 액면가
	private Double e_parp;
	// 거래량
	private Double tvol;
	// 거래대금
	private Double tamt;
	// ETP 분류명
	private String etyp_nm;

}
