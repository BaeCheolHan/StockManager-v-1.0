package com.my.stock.stockmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.my.stock.stockmanager.api.kis.KisApi;
import com.my.stock.stockmanager.dto.kis.request.OverSeaStockPriceRequest;
import com.my.stock.stockmanager.dto.kis.response.OverSeaNowStockPriceWrapper;
import com.my.stock.stockmanager.redis.entity.DividendInfo;
import com.my.stock.stockmanager.utils.KisApiUtils;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import java.net.MalformedURLException;

@SpringBootTest
class StockManagerApplicationTests {

	@Autowired
	private KisApi kisApi;

	@Autowired
	private KisApiUtils kisApiUtils;

	@Test
	void contextLoads() {
	}

	@Test
	public void getOverSea() throws MalformedURLException, JsonProcessingException {
		HttpHeaders headers = kisApiUtils.getDefaultApiHeader("HHDFS76200200");
		headers.add("custtype", "P");

		OverSeaNowStockPriceWrapper response = kisApi.getOverSeaStockPrice(headers, OverSeaStockPriceRequest.builder()
				.AUTH("")
				.EXCD("NAS")
				.SYMB("AAPL")
				.build());

		System.out.println(response);
	}

}
