package com.my.stock.stockmanager.dto.kis.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KrStockVolumeRankRequest {
	// 조건 시장 분류 코드(J)
	private String FID_COND_MRKT_DIV_CODE;
	// 	조건 화면 분류 코드 (20171)
	private String FID_COND_SCR_DIV_CODE;
	// 입력 종목코드  0000(전체) 기타(업종코드)
	private String FID_INPUT_ISCD;
	// 분류 구분 코드  	0(전체) 1(보통주) 2(우선주)
	private String FID_DIV_CLS_CODE;
	// 소속 구분 코드 0 : 평균거래량 1:거래증가율 2:평균거래회전율 3:거래금액순 4:평균거래금액회전율
	private String FID_BLNG_CLS_CODE;
	// 대상 구분 코드 	1 or 0 9자리 (차례대로 증거금 30% 40% 50% 60% 100% 신용보증금 30% 40% 50% 60%)
	//ex) "111111111"
	private String FID_TRGT_CLS_CODE;
	// 대상 제외 구분 코드 	1 or 0 6자리 (차례대로 투자위험/경고/주의 관리종목 정리매매 불성실공시 우선주 거래정지)
	//ex) "000000"
	private String FID_TRGT_EXLS_CLS_CODE;
	/**
	 * 입력 가격1
	 * 가격 ~
	 * ex) "0"
	 *
	 * 전체 가격 대상 조회 시 FID_INPUT_PRICE_1, FID_INPUT_PRICE_2 모두 ""(공란) 입력
	 */
	private String FID_INPUT_PRICE_1;

	/**
	 * 입력 가격2
	 * 가격 ~
	 * ex) "0"
	 *
	 * 전체 가격 대상 조회 시 FID_INPUT_PRICE_1, FID_INPUT_PRICE_2 모두 ""(공란) 입력
	 */
	private String FID_INPUT_PRICE_2;
	/**
	 * 거래량 수
	 * 	거래량 ~
	 * ex) "100000"
	 *
	 * 전체 거래량 대상 조회 시 FID_VOL_CNT ""(공란) 입력
	 */
	private String FID_VOL_CNT;

	// 입력 날짜1 	""(공란) 입력
	private String FID_INPUT_DATE_1;
}
