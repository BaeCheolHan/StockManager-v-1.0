package com.my.stock.stockmanager.api.kakao;

import com.my.stock.stockmanager.dto.social.kakao.KaKaoUserData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoApi", url = "https://kapi.kakao.com")
public interface KakaoApi {
    @PostMapping(value = "/v2/user/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KaKaoUserData getUserInfo(@RequestHeader HttpHeaders headers);
}


