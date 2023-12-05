package com.my.stock.stockmanager.dto.ebest.stock.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class t3320OutBlock1 {
	// 기업코드
	private String gicode;
	// 결산년월
	private String gsym;
	// 결산구분
	private String gsgb;
	// PER
	private BigDecimal per;
	// EPS
	private BigDecimal eps;
	// PBR
	private BigDecimal pbr;
	// ROA
	private BigDecimal roa;
	// ROE
	private BigDecimal roe;
	// EBITDA
	private BigDecimal ebitda;
	// EVEBITDA
	private BigDecimal evebitda;
	// 액면가
	private BigDecimal par;
	// SPS
	private BigDecimal sps;
	// CPS
	private BigDecimal cps;
	// BPS
	private BigDecimal bps;
	// T.PER
	private BigDecimal t_per;
	// 최근분기년도
	private String t_gsym;
}
