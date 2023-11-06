package com.my.stock.stockmanager.api.ecos;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;

@FeignClient(name = "ecos", url = "https://ecos.bok.or.kr")
public interface EcosApi {

	@GetMapping("")
	String getOverSeaStockPrice(URI apiURI);

}
