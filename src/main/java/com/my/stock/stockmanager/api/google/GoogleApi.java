package com.my.stock.stockmanager.api.google;

import com.my.stock.stockmanager.dto.social.google.response.GoogleUser;
import com.my.stock.stockmanager.dto.social.google.rqeust.GoogleUserInfoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleApi", url = "https://www.googleapis.com")
public interface GoogleApi {
	@GetMapping("/userinfo/v2/me")
	GoogleUser getTokenInfo(@RequestHeader HttpHeaders headers, @SpringQueryMap GoogleUserInfoRequest access_token);
}
