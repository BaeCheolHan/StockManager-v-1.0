package com.my.stock.stockmanager.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.stock.stockmanager.dto.kis.KisToken;
import com.my.stock.stockmanager.global.infra.ApiCaller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Component
public class KisTokenProvider {

	private final String accessTokenGenerateUrl;
	private final String appKey;
	private final String appSecret;
	private static KisToken token;

	public KisTokenProvider(@Value("${api.kis.access-token-generate-url}") String accessTokenGenerateUrl,
							@Value("${api.kis.appKey}") String appKey,
							@Value("${api.kis.app-secret}") String appSecret
	) throws Exception {
		this.appKey = appKey;
		this.accessTokenGenerateUrl = accessTokenGenerateUrl;
		this.appSecret = appSecret;
		HashMap<String, Object> param = new HashMap<>();
		param.put("grant_type", "client_credentials");
		param.put("appkey", appKey);
		param.put("appsecret", appSecret);

		token = new ObjectMapper()
				.readValue(ApiCaller.getInstance().post(accessTokenGenerateUrl, param), KisToken.class);
	}

	public KisToken getToken() throws Exception {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expired = LocalDateTime.parse(token.getAccess_token_token_expired(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		if (expired.isBefore(now)) {
			HashMap<String, Object> param = new HashMap<>();
			param.put("grant_type", "client_credentials");
			param.put("appkey", appKey);
			param.put("appsecret", appSecret);
			token = new ObjectMapper()
					.readValue(ApiCaller.getInstance().post(accessTokenGenerateUrl, param), KisToken.class);
			return token;
		}
		return token;
	}
}
