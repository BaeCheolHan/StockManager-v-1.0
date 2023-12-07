package com.my.stock.stockmanager.api.ebest;

import com.my.stock.stockmanager.dto.ebest.stock.response.FNGSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;


@FeignClient(name = "EbestApi", url = "https://openapi.ebestsec.co.kr:8080")
public interface EbestApi {

	@PostMapping("/stock/investinfo")
	FNGSummary getFNGSummaryData(@RequestHeader HttpHeaders headers, @RequestBody Map<String, Object> body);
}
