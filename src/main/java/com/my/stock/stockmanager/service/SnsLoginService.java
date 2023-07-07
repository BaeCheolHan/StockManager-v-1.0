package com.my.stock.stockmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.stock.stockmanager.constants.SnsType;
import com.my.stock.stockmanager.dto.bank.account.response.BankAccountDto;
import com.my.stock.stockmanager.dto.social.kakao.KaKaoUserData;
import com.my.stock.stockmanager.dto.social.kakao.response.KakaoLoginResponse;
import com.my.stock.stockmanager.dto.social.kakao.KakaoToken;
import com.my.stock.stockmanager.global.infra.ApiCaller;
import com.my.stock.stockmanager.rdb.entity.BankAccount;
import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import com.my.stock.stockmanager.rdb.entity.Member;
import com.my.stock.stockmanager.rdb.repository.ExchangeRateRepository;
import com.my.stock.stockmanager.rdb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SnsLoginService {

	@Value("${server.kakao-key}")
	private String clientId;

	@Value("${spring.kakao-callback-url}")
	private String kakaoCallbackUrl;

	private final MemberRepository memberRepository;

	private final ExchangeRateRepository exchangeRateRepository;

	@Transactional
	public KakaoLoginResponse kakaoLogin(String code) throws Exception {
		HashMap<String, Object> param = new HashMap<>();
		param.put("grant_type", "authorization_code");
		param.put("client_id", clientId);
		param.put("redirect_url", kakaoCallbackUrl);
		param.put("code", code);

		KakaoToken token = new ObjectMapper()
				.readValue(ApiCaller.getInstance().post("https://kauth.kakao.com/oauth/token", param, MediaType.APPLICATION_FORM_URLENCODED), KakaoToken.class);

		KaKaoUserData userInfo = getKakaoUserData(token.getAccess_token());
		Member entity = memberRepository.findByEmail(userInfo.getKakao_account().getEmail())
				.orElseGet(() -> memberRepository.save(Member.builder()
						.snsType(SnsType.KAKAO)
						.nickName(userInfo.getKakao_account().getProfile().getNickname())
						.email(userInfo.getKakao_account().getEmail())
						.build())
				);

		List<BankAccount> bankAccounts = entity.getBankAccount();
		List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
		KakaoLoginResponse resp = KakaoLoginResponse.builder()
				.memberId(entity.getId())
				.email(entity.getEmail())
				.profile(userInfo.getKakao_account().getProfile())
				.exchangeRate(exchangeRateList.get(exchangeRateList.size() -1))
				.build();

		if (bankAccounts != null) {
			resp.setBankAccounts(bankAccounts.stream().map(BankAccountDto::new).collect(Collectors.toList()));
		}
		return resp;
	}


	public KaKaoUserData getKakaoUserData(String accessToken) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.setContentType(MediaType.valueOf("application/x-www-form-urlencoded;charset=utf-8"));

		return ApiCaller.getInstance().post("https://kapi.kakao.com/v2/user/me", null, headers, MediaType.APPLICATION_FORM_URLENCODED, KaKaoUserData.class);

	}
}
