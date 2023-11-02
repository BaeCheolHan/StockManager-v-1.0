package com.my.stock.stockmanager.dto.kis.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KrDailyIndexChartPriceRequest {

	// 조건 시장 분류 코드  :  업종 : U
	private String FID_COND_MRKT_DIV_CODE;

	/**
	 * 업종 상세코드   
	 * 0001 : 종합
	 * 0002 : 대형주
	 * ...
	 * kis developer 포탈 (FAQ : 종목정보 다운로드 - 업종코드 참조)
	 * 
	 * idxcode.xlsx 참고
	 */
	private String FID_INPUT_ISCD;

	private String FID_INPUT_DATE_1;

	private String FID_INPUT_DATE_2;

	private String FID_PERIOD_DIV_CODE;
}
