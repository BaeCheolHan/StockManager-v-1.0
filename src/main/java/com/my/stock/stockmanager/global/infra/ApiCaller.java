package com.my.stock.stockmanager.global.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ApiCaller {

	private static ApiCaller instance;

	public static ApiCaller getInstance() {
		if (instance == null) instance = new ApiCaller();
		return instance;
	}

	private ApiCaller() {
	}



	public String get(String url, HashMap<String, Object> params) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder ucb = getUriComponentBuilder(new URL(url));
		if (params != null) params.forEach(ucb::queryParam);

		UriComponents uc = ucb.build().encode();
		ResponseEntity<String> responseEntity = restTemplate.exchange(uc.toUri(), HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class);
		return responseEntity.getBody();
	}

	public String post(String url, HashMap<String, Object> param) throws Exception {
		RestTemplate restTemplate = new RestTemplateBuilder()
				.setConnectTimeout(Duration.ofSeconds(10))
				.setReadTimeout(Duration.ofSeconds(10))
				.build();

		UriComponentsBuilder ucb = getUriComponentBuilder(new URL(url));
		UriComponents uc = ucb.build().encode();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(param);
		HttpEntity<String> entity = new HttpEntity<>(json, headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(uc.toUri(), HttpMethod.POST, entity, String.class);
		return responseEntity.getBody();
	}

	public <T> T post(String url, HashMap<String, Object> param, HttpHeaders headers, MediaType mediaType, Class<T> type) throws Exception {
		RestTemplate restTemplate = new RestTemplateBuilder()
				.setConnectTimeout(Duration.ofSeconds(10))
				.setReadTimeout(Duration.ofSeconds(10))
				.build();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			public boolean hasError(ClientHttpResponse response) throws IOException {
				HttpStatus statusCode = (HttpStatus) response.getStatusCode();
				return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
			}
		});

		UriComponentsBuilder ucb = getUriComponentBuilder(new URL(url));
		UriComponents uc = ucb.build().encode();
		ObjectMapper objectMapper = new ObjectMapper();
		if(param == null) {
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<T> responseEntity = restTemplate.exchange(uc.toUri(), HttpMethod.POST, entity, type);
			return responseEntity.getBody();
		} else {
			String paramString = "";
			if(mediaType.equals(MediaType.APPLICATION_JSON)) {
				paramString = objectMapper.writeValueAsString(param);
			} else if(mediaType.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
				StringBuilder sb = new StringBuilder();
				for (Map.Entry<?,?> entry : param.entrySet()) {
					if (sb.length() > 0) {
						sb.append("&");
					}
					sb.append(String.format("%s=%s",
							urlEncodeUTF8(entry.getKey().toString()),
							urlEncodeUTF8(entry.getValue().toString())
					));
				}
				paramString = sb.toString();
			}

			HttpEntity<String> entity = new HttpEntity<>(paramString, headers);

			ResponseEntity<T> responseEntity = restTemplate.exchange(uc.toUri(), HttpMethod.POST, entity, type);
			return responseEntity.getBody();
		}

	}

	public String post(String url, HashMap<String, Object> param, MediaType mediaType) throws Exception {
		RestTemplate restTemplate = new RestTemplateBuilder()
				.setConnectTimeout(Duration.ofSeconds(10))
				.setReadTimeout(Duration.ofSeconds(10))
				.build();

		UriComponentsBuilder ucb = getUriComponentBuilder(new URL(url));
		UriComponents uc = ucb.build().encode();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		ObjectMapper objectMapper = new ObjectMapper();
		String paramString = "";

		if(mediaType.equals(MediaType.APPLICATION_JSON)) {
			paramString = objectMapper.writeValueAsString(param);
		} else if(mediaType.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<?,?> entry : param.entrySet()) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(String.format("%s=%s",
						urlEncodeUTF8(entry.getKey().toString()),
						urlEncodeUTF8(entry.getValue().toString())
				));
			}
			paramString = sb.toString();
		}

		HttpEntity<String> entity = new HttpEntity<>(paramString, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(uc.toUri(), HttpMethod.POST, entity, String.class);
		return responseEntity.getBody();
	}



	private UriComponentsBuilder getUriComponentBuilder(URL url) {
		return UriComponentsBuilder.newInstance()
				.scheme(url.getProtocol())
				.host(url.getHost())
				.port(url.getPort())
				.path(url.getPath());
	}

	private String urlEncodeUTF8(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}
}
