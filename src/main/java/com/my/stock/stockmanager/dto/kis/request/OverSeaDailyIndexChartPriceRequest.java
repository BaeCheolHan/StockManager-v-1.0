package com.my.stock.stockmanager.dto.kis.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OverSeaDailyIndexChartPriceRequest {
	// FID 조건 시장 분류 코드, N: 해외지수, X 환율
	private String FID_COND_MRKT_DIV_CODE;
	/**
	 *FID 입력 종목코드
	 *
	 * 	종목코드
	 * ※ 해외주식 마스터 코드 참조
	 * (포럼 > FAQ > 종목정보 다운로드 > 해외주식)
	 *
	 * ※ 해당 API로 미국주식 조회 시, 다우30, 나스닥100, S&P500 종목만 조회 가능합니다. 더 많은 미국주식 종목 시세를 이용할 시에는, 해외주식기간별시세 API 사용 부탁드립니다.
	 */
	private String FID_INPUT_ISCD;
	// 시작일자(YYYYMMDD)
	private String FID_INPUT_DATE_1;
	// 	종료일자(YYYYMMDD)
	private String FID_INPUT_DATE_2;
	// FID 기간 분류 코드 D:일, W:주, M:월, Y:년
	private String FID_PERIOD_DIV_CODE;

//	해외지수 시세는 해외주식현재가 → 해외주식 종목/지수/환율기간별시세(일/주/월/년) API 이용하셔서 조회 가능하며,
//	나스닥 종합주가지수는 FID_INPUT_ISCD:COMP 입니다.
}
