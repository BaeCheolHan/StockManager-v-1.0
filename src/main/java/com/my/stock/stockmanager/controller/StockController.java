package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stock.DashboardStock;
import com.my.stock.stockmanager.dto.stock.request.StockSaveRequest;
import com.my.stock.stockmanager.dto.stock.response.DashboardStockResponse;
import com.my.stock.stockmanager.dto.stock.response.DetailStockInfo;
import com.my.stock.stockmanager.dto.stock.response.DetailStockInfoResponse;
import com.my.stock.stockmanager.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

	@PostMapping
	public BaseResponse saveStock(@RequestBody StockSaveRequest request) throws Exception {
		service.saveStock(request);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@GetMapping("/{memberId}/{national}/{code}/{symbol}")
	public DetailStockInfoResponse getDetail(@PathVariable Long memberId, @PathVariable String national, @PathVariable String code, @PathVariable String symbol) {
		DetailStockInfo detail = service.getDetail(memberId, national, code, symbol);
		return DetailStockInfoResponse.builder().detail(detail).code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage()).build();
	}

	@DeleteMapping("/{id}")
	public BaseResponse deleteStock(@PathVariable Long id) {
		service.deleteById(id);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}
}
