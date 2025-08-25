package com.my.stock.stockmanager.api.kakao;

import com.my.stock.stockmanager.dto.social.kakao.request.KakaoTokenRequest;
import com.my.stock.stockmanager.dto.social.kakao.KakaoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoAuthApi", url = "https://kauth.kakao.com")
public interface KakaoAuthApi {
    @PostMapping("/oauth/token")
    KakaoToken getToken(@RequestHeader HttpHeaders headers, @SpringQueryMap KakaoTokenRequest param);
}


