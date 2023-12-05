package com.my.stock.stockmanager.api.ebest;

import com.my.stock.stockmanager.config.FormFeignEncoderConfig;
import com.my.stock.stockmanager.dto.ebest.token.request.EbestTokenRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "EbestFormApi", url = "https://openapi.ebestsec.co.kr:8080", configuration = FormFeignEncoderConfig.class)
public interface EbestFormApi {

	@PostMapping(value = "/oauth2/token", consumes = "application/x-www-form-urlencoded")
	String getToken(@RequestBody EbestTokenRequest param);
}
