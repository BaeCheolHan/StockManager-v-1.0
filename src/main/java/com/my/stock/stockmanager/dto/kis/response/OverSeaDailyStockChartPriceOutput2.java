package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverSeaDailyStockChartPriceOutput2 {
	// 일자(YYYYMMDD)
	private String xymd;
	// 종가
	private String clos;
	/**
	 * 대비기호
	 * 1 : 상한
	 * 2 : 상승
	 * 3 : 보합
	 * 4 : 하한
	 * 5 : 하락
	 */
	private String sign;
	//	대비	해당 일자의 종가와 해당 전일 종가의 차이 (해당일 종가-해당 전일 종가)
	private String diff;
	//	등락율	해당 전일 대비 / 해당일 종가 * 100
	private String rate;
	//	시가	해당일 최초 거래가격
	private String open;
	//	고가	해당일 가장 높은 거래가격
	private String high;
	// 저가 해당일 가장 낮은 거래가격
	private String low;
	//	거래량	해당일 거래량
	private String tvol;
	//	거래대금	해당일 거래대금
	private String tamt;
	//	매수호가	마지막 체결이 발생한 시점의 매수호가
	//* 해당 일자 거래량 0인 경우 값이 수신되지 않음
	private String pbid;

	//매수호가잔량	* 해당 일자 거래량 0인 경우 값이 수신되지 않음
	private String vbid;
	// 매도호가	마지막 체결이 발생한 시점의 매도호가
	//  * 해당 일자 거래량 0인 경우 값이 수신되지 않음
	private String pask;
	// 매도호가잔량	해당 일자 거래량 0인 경우 값이 수신되지 않음
	private String vask;
}
