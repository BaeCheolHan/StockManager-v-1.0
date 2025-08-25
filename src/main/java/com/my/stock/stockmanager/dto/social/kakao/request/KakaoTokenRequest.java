package com.my.stock.stockmanager.dto.social.kakao.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoTokenRequest {
    private String grant_type = "authorization_code";
    private String client_id;
    private String redirect_url;
    private String code;
}


