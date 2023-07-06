package com.my.stock.stockmanager.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockPriceSchedule {

//	private final KisTokenProvider kisTokenGenerator;
//	@Value("${api.kis.access-token-generate-url}")
//	private String accessTokenGenerateUrl;
//
//	@Value("${api.kis.appKey}")
//	private String appKey;
//
//	@Value("${api.kis.app-secret}")
//	private String appSecret;
//
//	@Value("${api.kis.user-id}")
//	private String userId;
//
//	@Async
//	@Scheduled(fixedRate = 1000 * 10 * 30)
//	public void scheduleFixedExchangeDollarRateTask() throws Exception {
//		KisToken token = kisTokenGenerator.getToken();
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("authorization", "Bearer " + token.getAccess_token());
//		headers.add("appkey", appKey);
//		headers.add("appsecret", appSecret);
//		headers.add("custtype", "P");
//		headers.add("tr_id", "HHKST03900300");
//
//		//tr_cont	연속 거래 여부	String	N	1	공백 : 초기 조회
//		//N : 다음 데이터 조회 (output header의 tr_cont가 M일 경우)
//
//		headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
//		HashMap<String, Object> param = new HashMap<>();
//		param.put("user_id", userId);
//		String response = ApiCaller.getInstance().post("https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/quotations/psearch-title", param, headers, MediaType.APPLICATION_JSON, String.class);
//		System.out.println(response);
//	}
}
