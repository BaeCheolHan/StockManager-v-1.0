package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.member.social.KakaoLogin;
import com.my.stock.stockmanager.constants.ResponseCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
	@Value("${server.kakao-key}")
	private String code;

	@Value("${spring.kakao-callback-url}")
	private String kakaoCallbackUrl;

	@GetMapping("/kakao")
	public ResponseEntity<KakaoLogin> loginWithKakao() {
		return new ResponseEntity<>(KakaoLogin.builder()
				.loginUri(String.format("https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code", code, kakaoCallbackUrl))
				.code(ResponseCode.SUCCESS)
				.message(ResponseCode.SUCCESS.getMessage())
				.build(), HttpStatus.OK);
	}
}
