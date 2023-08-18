package com.my.stock.stockmanager.api.kis;

import com.my.stock.stockmanager.dto.kis.request.KrStockPriceRequest;
import com.my.stock.stockmanager.dto.kis.request.OverSeaStockPriceRequest;
import com.my.stock.stockmanager.dto.stock.KrNowStockPriceWrapper;
import com.my.stock.stockmanager.dto.stock.OverSeaNowStockPriceWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KisApi", url = "https://openapi.koreainvestment.com:9443/")
public interface KisApi {

	@GetMapping("uapi/overseas-price/v1/quotations/price-detail")
	OverSeaNowStockPriceWrapper getOverSeaStockPrice(@RequestHeader HttpHeaders header, @SpringQueryMap OverSeaStockPriceRequest param);

	@GetMapping("uapi/domestic-stock/v1/quotations/inquire-price")
	KrNowStockPriceWrapper getKorStockPrice(@RequestHeader HttpHeaders header, @SpringQueryMap KrStockPriceRequest param);
}
