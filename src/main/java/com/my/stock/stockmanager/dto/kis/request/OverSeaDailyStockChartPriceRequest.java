package com.my.stock.stockmanager.dto.kis.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OverSeaDailyStockChartPriceRequest {
	// 사용자권한정보  	"" (Null 값 설정)
	private String AUTH;

	/**
	 * 거래소 코드
	 * HKS : 홍콩
	 * NYS : 뉴욕
	 * NAS : 나스닥
	 * AMS : 아멕스
	 * TSE : 도쿄
	 * SHS : 상해
	 * SZS : 심천
	 * SHI : 상해지수
	 * SZI : 심천지수
	 * HSX : 호치민
	 * HNX : 하노이
	 * BAY : 뉴욕(주간)
	 * BAQ : 나스닥(주간)
	 * BAA : 아멕스(주간)
	 */
	private String EXCD;

	// 종목코드
	private String SYMB;

	/**
	 * 0 : 일
	 * 1 : 주
	 * 2 : 월
	 */
	private String GUBN;

	/**
	 * 조회기준일자(YYYYMMDD)
	 * ※ 공란 설정 시, 기준일 오늘 날짜로 설정
	 */
	private String BYMD;

	/**
	 * 수정주가반영여부
	 * 0 : 미반영
	 * 1 : 반영
	 */
	private String MODP;

	/**
	 * NEXT KEY BUFF
	 * 	응답시 다음값이 있으면 값이 셋팅되어 있으므로 다음 조회시 응답값 그대로 셋팅
	 */
	private String KEYB;

}
