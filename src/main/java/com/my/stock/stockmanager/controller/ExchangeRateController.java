package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import com.my.stock.stockmanager.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/api/exchange-rate")
@RestController
@RequiredArgsConstructor
public class ExchangeRateController {

	private final ExchangeRateService exchangeRateService;

	@GetMapping
	public ExchangeRate getExchangeRate() throws IOException {
		return exchangeRateService.getExchangeRate();
	}

	@GetMapping("/us")
	public void getUSExchageRate() {
		exchangeRateService.test();
	}
}
