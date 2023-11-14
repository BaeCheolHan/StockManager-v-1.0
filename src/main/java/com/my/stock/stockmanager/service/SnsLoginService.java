package com.my.stock.stockmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.stock.stockmanager.api.google.GoogleApi;
import com.my.stock.stockmanager.api.google.GoogleAuthApi;
import com.my.stock.stockmanager.constants.SnsType;
import com.my.stock.stockmanager.dto.bank.account.response.BankAccountDto;
import com.my.stock.stockmanager.dto.social.google.response.GoogleToken;
import com.my.stock.stockmanager.dto.social.google.response.GoogleUser;
import com.my.stock.stockmanager.dto.social.google.rqeust.GoogleTokenRequest;
import com.my.stock.stockmanager.dto.social.google.rqeust.GoogleUserInfoRequest;
import com.my.stock.stockmanager.dto.social.kakao.KaKaoUserData;
import com.my.stock.stockmanager.dto.social.kakao.KakaoToken;
import com.my.stock.stockmanager.dto.social.kakao.response.LoginResponse;
import com.my.stock.stockmanager.global.infra.ApiCaller;
import com.my.stock.stockmanager.rdb.entity.*;
import com.my.stock.stockmanager.rdb.repository.ExchangeRateRepository;
import com.my.stock.stockmanager.rdb.repository.LoginHistoryRepository;
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

	@Value("${api.google.client_id}")
	private String googleClientId;

	@Value("${api.google.client_secret}")
	private String googleClientSecret;

	@Value("${spring.kakao-callback-url}")
	private String kakaoCallbackUrl;

	@Value("${spring.google-callback-url}")
	private String googleCallbackUrl;

	private final MemberRepository memberRepository;
	private final ExchangeRateRepository exchangeRateRepository;
	private final LoginHistoryRepository loginHistoryRepository;

	private final GoogleAuthApi googleAuthApi;
	private final GoogleApi googleApi;


	@Transactional
	public LoginResponse kakaoLogin(String code) throws Exception {
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
						.loginId(userInfo.getId())
						.build())
				);

		if (entity.getLoginId() == null) {
			entity.setLoginId(userInfo.getId());
			memberRepository.save(entity);
		}

		List<BankAccount> bankAccounts = entity.getBankAccount();
		List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
		PersonalSetting setting = entity.getPersonalSetting();
		LoginResponse resp = LoginResponse.builder()
				.memberId(entity.getId())
				.email(entity.getEmail())
				.profileImgUrl(userInfo.getKakao_account().getProfile().getThumbnail_image_url().replace("http://", "https://"))
				.nickName(userInfo.getKakao_account().getProfile().getNickname())
				.exchangeRate(exchangeRateList.get(exchangeRateList.size() - 1))
				.build();

		if (bankAccounts != null) {
			resp.setBankAccounts(bankAccounts.stream().map(BankAccountDto::new).collect(Collectors.toList()));
		}

		if (setting != null) {
			resp.setDefaultBankAccountId(setting.getDefaultBankAccountId());
		}

		LoginHistory history = new LoginHistory();
		history.setMember(entity);
		loginHistoryRepository.save(history);
		return resp;
	}


	public KaKaoUserData getKakaoUserData(String accessToken) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.setContentType(MediaType.valueOf("application/x-www-form-urlencoded;charset=utf-8"));

		return ApiCaller.getInstance().post("https://kapi.kakao.com/v2/user/me", null, headers, MediaType.APPLICATION_FORM_URLENCODED, KaKaoUserData.class);
	}

	@Transactional
	public LoginResponse googleLogin(String code, String scope) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		GoogleTokenRequest googleTokenRequest = new GoogleTokenRequest();
		googleTokenRequest.setRedirect_uri(googleCallbackUrl);
		googleTokenRequest.setCode(code);
		googleTokenRequest.setClient_id(googleClientId);
		googleTokenRequest.setClient_secret(googleClientSecret);

		GoogleToken googleToken = googleAuthApi.getGoogleToken(headers, googleTokenRequest);

		GoogleUserInfoRequest googleUserInfoRequest = new GoogleUserInfoRequest();
		googleUserInfoRequest.setAccess_token(googleToken.getAccess_token());

		GoogleUser userInfo = googleApi.getTokenInfo(headers, googleUserInfoRequest);

		Member entity = memberRepository.findById(userInfo.getId())
				.orElseGet(() -> memberRepository.save(Member.builder()
						.id(userInfo.getId())
						.snsType(SnsType.KAKAO)
						.nickName(userInfo.getName())
						.loginId(userInfo.getId())
						.build())
				);

		List<BankAccount> bankAccounts = entity.getBankAccount();
		List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
		PersonalSetting setting = entity.getPersonalSetting();
		LoginResponse resp = LoginResponse.builder()
				.memberId(entity.getId())
				.email(entity.getEmail())
				.profileImgUrl(userInfo.getPicture())
				.nickName(userInfo.getName())
				.exchangeRate(exchangeRateList.get(exchangeRateList.size() - 1))
				.build();

		if (bankAccounts != null) {
			resp.setBankAccounts(bankAccounts.stream().map(BankAccountDto::new).collect(Collectors.toList()));
		}

		if (setting != null) {
			resp.setDefaultBankAccountId(setting.getDefaultBankAccountId());
		}

		LoginHistory history = new LoginHistory();
		history.setMember(entity);
		loginHistoryRepository.save(history);
		return resp;
	}
}
