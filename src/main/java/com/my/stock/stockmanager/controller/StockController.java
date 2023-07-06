package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

	private final StockService service;

	@GetMapping({"/{memberId}", "/{memberId}/{bankId}"})
	public void getStocks(@PathVariable Long memberId, @PathVariable(required = false) Long bankId) {
		service.getStocks(memberId, bankId);
		System.out.println(memberId);
		System.out.println(bankId);

	}
}
