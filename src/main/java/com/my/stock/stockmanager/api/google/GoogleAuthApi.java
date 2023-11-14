package com.my.stock.stockmanager.api.google;

import com.my.stock.stockmanager.dto.social.google.response.GoogleToken;
import com.my.stock.stockmanager.dto.social.google.rqeust.GoogleTokenRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleAuthApi", url="https://oauth2.googleapis.com")
public interface GoogleAuthApi {
	@PostMapping("/token")
	GoogleToken getGoogleToken(@RequestHeader HttpHeaders headers, @SpringQueryMap GoogleTokenRequest param);

}