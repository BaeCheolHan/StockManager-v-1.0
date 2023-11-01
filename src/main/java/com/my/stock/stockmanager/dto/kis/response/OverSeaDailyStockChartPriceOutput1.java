package com.my.stock.stockmanager.dto.kis.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverSeaDailyStockChartPriceOutput1 {
	/**
	 * 실시간조회종목코드
	 * D+시장구분(3자리)+종목코드
	 * 예) DNASAAPL : D+NAS(나스닥)+AAPL(애플)
	 * [시장구분]
	 * NYS : 뉴욕, NAS : 나스닥, AMS : 아멕스 ,
	 * TSE : 도쿄, HKS : 홍콩,
	 * SHS : 상해, SZS : 심천
	 * HSX : 호치민, HNX : 하노이
	 */
	private String rsym;
	// 소수점자리수
	private String zdiv;
	// 전일종가
	private String nrec;
}
