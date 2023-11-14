package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.dto.social.kakao.SnsLoginRequestInfo;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.exception.StockManagerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
	@Value("${server.kakao-key}")
	private String code;

	@Value("${spring.kakao-callback-url}")
	private String kakaoCallbackUrl;

	@Value("${api.google.client_id}")
	private String googleClientId;

	@Value("${spring.google-callback-url}")
	private String googleCallbackUrl;

	@GetMapping("/{snsType}")
	public ResponseEntity<SnsLoginRequestInfo> loginWithSns(@PathVariable String snsType) throws StockManagerException {

		switch (snsType) {
			case "kakao":
				return new ResponseEntity<>(SnsLoginRequestInfo.builder()
						.loginUri(String.format("https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code", code, kakaoCallbackUrl))
						.code(ResponseCode.SUCCESS)
						.message(ResponseCode.SUCCESS.getMessage())
						.build(), HttpStatus.OK);
			case "google":
				return new ResponseEntity<>(SnsLoginRequestInfo.builder()
						.loginUri(String.format("https://accounts.google.com/o/oauth2/v2/auth?client_id=%s&scope=profile&response_type=code&redirect_uri=%s", googleClientId, googleCallbackUrl))
						.code(ResponseCode.SUCCESS)
						.message(ResponseCode.SUCCESS.getMessage())
						.build(), HttpStatus.OK);
			default:
				throw new StockManagerException(ResponseCode.INVALID_ARGUMENT);
		}


	}
}
