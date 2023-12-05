package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.api.ebest.EbestApi;
import com.my.stock.stockmanager.dto.ebest.stock.request.t3320InBlock;
import com.my.stock.stockmanager.dto.ebest.stock.response.FNGSummary;
import com.my.stock.stockmanager.utils.EbestTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class Test {

	private final EbestTokenProvider ebestTokenProvider;
	private final EbestApi ebestApi;

	@GetMapping
	public void test() throws Exception {
		ebestTokenProvider.getRestToken();


		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json; charset=utf-8");
		headers.add("authorization", "Bearer " + ebestTokenProvider.getRestToken().getAccess_token());
		headers.add("tr_cd", "t3320");
		headers.add("tr_cont", "N");
		headers.add("tr_cont_key", "");

		t3320InBlock request = new t3320InBlock();
		request.setGicode("005930");

		Map<String, Object> req = new HashMap<>();
		req.put("t3320InBlock", request);

		FNGSummary resp = ebestApi.getFNGSummaryData(headers, req);
		System.out.println(resp);
	}
}
