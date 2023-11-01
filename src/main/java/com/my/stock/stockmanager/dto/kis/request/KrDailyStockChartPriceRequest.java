package com.my.stock.stockmanager.dto.kis.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KrDailyStockChartPriceRequest {
	// 시장분류코드 J : 주식, ETF, ETN
	private String FID_COND_MRKT_DIV_CODE;

	// 종목 코드 : 종목번호 (6자리)
	//ETN의 경우, Q로 시작 (EX. Q500001)
	private String FID_INPUT_ISCD;

	//입력 날짜 (시작) : 조회 시작일자 (ex. 20220501)
	private String FID_INPUT_DATE_1;

	// 입력 날짜 (종료) : 조회 종료일자 (ex. 20220530)
	private String FID_INPUT_DATE_2;

	// 기간분류코드 : D:일봉, W:주봉, M:월봉, Y:년봉
	private String FID_PERIOD_DIV_CODE;

	// 수정주가 원주가 가격 여부 : 	0:수정주가 1:원주가
	private String FID_ORG_ADJ_PRC;

}
