package com.my.stock.stockmanager.utils;

import com.my.stock.stockmanager.redis.entity.RestKisToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KisApiUtils {

	@Value("${api.kis.appKey}")
	private String appKey;
	@Value("${api.kis.app-secret}")
	private String appSecret;

	private final KisTokenProvider kisTokenProvider;

	public HttpHeaders getDefaultApiHeader() throws Exception {
		RestKisToken kisToken = kisTokenProvider.getRestToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("authorization", String.format("%s %s", kisToken.getToken_type(), kisToken.getAccess_token()));
		headers.add("content-type", "application/json; charset=utf-8");
		headers.add("appkey", appKey);
		headers.add("appsecret", appSecret);

		return headers;
	}
}
