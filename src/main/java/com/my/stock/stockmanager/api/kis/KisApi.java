package com.my.stock.stockmanager.api.kis;

import com.my.stock.stockmanager.dto.kis.request.OverSeaStockPriceRequest;
import com.my.stock.stockmanager.dto.stock.OverSeaNowStockPriceWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KisApi", url = "https://openapi.koreainvestment.com:9443/")
public interface KisApi {

	@GetMapping("uapi/overseas-price/v1/quotations/price-detail")
	OverSeaNowStockPriceWrapper getOverSeaStockPrice(@RequestHeader HttpHeaders header, @RequestParam OverSeaStockPriceRequest param);
}
