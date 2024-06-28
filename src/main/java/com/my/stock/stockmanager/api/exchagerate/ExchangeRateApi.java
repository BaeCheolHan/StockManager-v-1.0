package com.my.stock.stockmanager.api.exchagerate;

import com.my.stock.stockmanager.dto.exchangerate.ExchangeRateDto;
import com.my.stock.stockmanager.dto.exchangerate.request.ExchangeRateApiRequest;
import com.my.stock.stockmanager.dto.social.google.rqeust.GoogleUserInfoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.util.List;

@FeignClient(name = "koreaxim", url = "https://www.koreaexim.go.kr/")
public interface ExchangeRateApi {

	@GetMapping("/site/program/financial/exchangeJSON")
	List<ExchangeRateDto> getExchangeRate(@SpringQueryMap ExchangeRateApiRequest request);

}
