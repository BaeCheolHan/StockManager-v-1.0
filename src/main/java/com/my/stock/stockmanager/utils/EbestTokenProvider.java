package com.my.stock.stockmanager.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.stock.stockmanager.dto.ebest.token.request.EbestTokenRequest;
import com.my.stock.stockmanager.global.infra.ApiCaller;
import com.my.stock.stockmanager.redis.entity.RestEbestToken;
import com.my.stock.stockmanager.redis.entity.RestKisToken;
import com.my.stock.stockmanager.redis.repository.RestEbestTokenRepository;
import com.my.stock.stockmanager.redis.repository.RestKisTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class EbestTokenProvider {

	@Value("${api.ebest.appKey}")
	private String appKey;
	@Value("${api.ebest.app-secret}")
	private String appSecret;

	@Value("${api.ebest.access-token-generate-url}")
	private String accessTokenGenerateUrl;
	private final RestEbestTokenRepository restEbestTokenRepository;
	public RestEbestToken getRestToken() throws Exception {

		List<RestEbestToken> list = (List<RestEbestToken>) restEbestTokenRepository.findAll();

		list= list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			EbestTokenRequest request = new EbestTokenRequest();
			request.setAppkey(appKey);
			request.setAppsecretkey(appSecret);

			RestEbestToken restEbestToken = new ObjectMapper()
					.readValue(ApiCaller.getInstance().post(accessTokenGenerateUrl, request.toMap(), MediaType.APPLICATION_FORM_URLENCODED), RestEbestToken.class);

			restEbestTokenRepository.save(restEbestToken);

			return restEbestToken;
		}
	}
}
