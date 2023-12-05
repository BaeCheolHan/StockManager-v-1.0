package com.my.stock.stockmanager.dto.ebest.stock.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class t3320OutBlock {
	// 업종구분명
	private String upgubunnm;
	// 시장구분
	private String sijangcd;
	// 시장구분명
	private String marketnm;
	// 한글기업명
	private String company;
	// 본사주소
	private String baddress;
	// 본사전화번호
	private String btelno;
	// 최근결산년도
	private String gsyyyy;
	// 결산월
	private String gsmm;
	// 최근결산년월
	private String gsym;
	// 주당액면가
	private BigDecimal lstprice;
	// 주식수
	private BigDecimal gstock;
	// Homepage
	private String homeurl;
	// 그룹명
	private String grdnm;
	// 외국인
	private BigDecimal foreignratio;
	// 주담전화
	private String irtel;
	// 자본금
	private BigDecimal capital;
	// 시가총액
	private BigDecimal sigavalue;
	// 배당금
	private BigDecimal cashsis;
	// 배당수익율
	private BigDecimal cashrate;
	// 현재가
	private BigDecimal price;
	// 전일종가
	private BigDecimal jnilclose;
	// 위험고지구분1_정리매매
	private String notice1;
	// 위험고지구분2_투자위험
	private String notice2;
	// 	위험고지구분3_단기과열
	private String notice3;
}
