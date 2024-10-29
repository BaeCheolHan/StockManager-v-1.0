package com.my.stock.stockmanager.global.infra;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Map;

public class ApiCaller {

	private static ApiCaller instance;

	public static ApiCaller getInstance() {
		if (instance == null) instance = new ApiCaller();
		return instance;
	}

	private ApiCaller() {}

	public String get(String url) throws Exception {
		RestTemplate restTemplate = createRestTemplate();
		System.out.println("call");
		ignoreCertificates();
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class);
		return responseEntity.getBody();
	}

	private RestTemplate createRestTemplate() throws Exception {
		SSLContext sslContext = SSLContextBuilder.create()
				.loadTrustMaterial((chain, authType) -> true)  // 모든 인증서 신뢰
				.build();

		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
				sslContext,
				NoopHostnameVerifier.INSTANCE  // 호스트 이름 검증 비활성화
		);

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", socketFactory)
				.build();

		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManagerShared(true)
				.setConnectionManager(new org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager(socketFactoryRegistry))
				.build();

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		return new RestTemplate(factory);
	}

	public String get(String url, Map<String, Object> params) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder ucb = getUriComponentBuilder(new URL(url));
		if (params != null) params.forEach(ucb::queryParam);

		UriComponents uc = ucb.build().encode();
		ResponseEntity<String> responseEntity = restTemplate.exchange(uc.toUri(), HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class);
		return responseEntity.getBody();
	}

	public String get(String url, HttpHeaders headers, Map<String, Object> params) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder ucb = getUriComponentBuilder(new URL(url));
		if (params != null) params.forEach(ucb::queryParam);

		UriComponents uc = ucb.build().encode();
		ResponseEntity<String> responseEntity = restTemplate.exchange(uc.toUri(), HttpMethod.GET, new HttpEntity<String>(headers), String.class);
		return responseEntity.getBody();
	}

	public String post(String url, Map<String, Object> param) throws MalformedURLException, JsonProcessingException {
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

	public <T> T post(String url, Map<String, Object> param, HttpHeaders headers, MediaType mediaType, Class<T> type) throws Exception {
		RestTemplate restTemplate = new RestTemplateBuilder()
				.setConnectTimeout(Duration.ofSeconds(10))
				.setReadTimeout(Duration.ofSeconds(10))
				.build();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			public boolean hasError(ClientHttpResponse response) {
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
					if (!sb.isEmpty()) {
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

	public String post(String url, Map<String, Object> param, MediaType mediaType) throws Exception {
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
				if (!sb.isEmpty()) {
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
		return URLEncoder.encode(s, StandardCharsets.UTF_8);
	}


	private static void ignoreCertificates() {
		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		}};

		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {

		}

	}
}
