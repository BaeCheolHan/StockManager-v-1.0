package com.my.stock.stockmanager.dto.social.kakao;

import lombok.Getter;

@Getter
public class KaKaoUserData {
    private String id;
    private String connected_at;
    private KaKaoProperties properties;
    private KakaoAccount kakao_account;
}

