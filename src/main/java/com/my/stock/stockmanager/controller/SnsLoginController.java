package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.dto.social.kakao.response.KakaoLoginResponse;
import com.my.stock.stockmanager.service.SnsLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sns/login")
public class SnsLoginController {

	private final SnsLoginService snsLoginService;

	@GetMapping("/kakao")
	public KakaoLoginResponse kakao(String code) throws Exception {
		return snsLoginService.kakaoLogin(code);
	}

}
