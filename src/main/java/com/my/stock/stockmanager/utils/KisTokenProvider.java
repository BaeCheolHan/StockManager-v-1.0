package com.my.stock.stockmanager.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.stock.stockmanager.global.infra.ApiCaller;
import com.my.stock.stockmanager.redis.entity.RestKisToken;
import com.my.stock.stockmanager.redis.repository.RestKisTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class KisTokenProvider {

	@Value("${api.kis.access-token-generate-url}")
	private String accessTokenGenerateUrl;
	@Value("${api.kis.appKey}")
	private String appKey;
	@Value("${api.kis.app-secret}")
	private String appSecret;
	private final RestKisTokenRepository restKisTokenRepository;


	public RestKisToken getRestToken() throws Exception {

		List<RestKisToken> l = (List<RestKisToken>) restKisTokenRepository.findAll();

		if (!l.isEmpty()) {
			return l.get(0);
		} else {
			HashMap<String, Object> param = new HashMap<>();
			param.put("grant_type", "client_credentials");
			param.put("appkey", appKey);
			param.put("appsecret", appSecret);

			RestKisToken restKisToken = new ObjectMapper()
					.readValue(ApiCaller.getInstance().post(accessTokenGenerateUrl, param), RestKisToken.class);
			restKisToken.setExpires_in(restKisToken.getExpires_in() - 10000);
			restKisTokenRepository.save(restKisToken);
			return restKisToken;
		}
	}

}
