package com.my.stock.stockmanager.schedule;

import com.my.stock.stockmanager.global.infra.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class StockPriceSchedule {
	@Value("${api.kis.access-token-generate-url}")
	private String accessTokenGenerateUrl;

	@Value("${api.kis.appKey}")
	private String appKey;

	@Value("${api.kis.app-secret}")
	private String appSecret;

	@Async
	@Scheduled(fixedRate = 1000 * 10 * 30)
	public void scheduleFixedExchangeDollarRateTask() throws Exception {

		HashMap<String, Object> param = new HashMap<>();
		param.put("grant_type", "client_credentials");
		param.put("appkey", appKey);
		param.put("appsecret", appSecret);


		String response = ApiCaller.getInstance().post(accessTokenGenerateUrl, param);

	}
}
