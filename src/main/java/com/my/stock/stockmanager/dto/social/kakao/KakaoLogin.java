package com.my.stock.stockmanager.dto.social.kakao;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoLogin extends BaseResponse {
	private String loginUri;
	@Builder
	public KakaoLogin(ResponseCode code, String message, String loginUri) {
		super(code, message);
		this.loginUri = loginUri;
	}
}
