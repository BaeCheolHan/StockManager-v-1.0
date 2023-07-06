package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stock.DashboardStock;
import com.my.stock.stockmanager.dto.stock.response.DashboardStockResponse;
import com.my.stock.stockmanager.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

	private final StockService service;

	@GetMapping({"/{memberId}", "/{memberId}/{bankId}"})
	public DashboardStockResponse getStocks(@PathVariable Long memberId, @PathVariable(required = false) Long bankId) {
		List<DashboardStock> data = service.getStocks(memberId, bankId);
		return DashboardStockResponse.builder().stocks(data).code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage()).build();
	}
}
