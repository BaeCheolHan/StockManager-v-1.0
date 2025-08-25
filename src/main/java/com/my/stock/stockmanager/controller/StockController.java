package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ChartType;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stock.request.StockSaveRequest;
import com.my.stock.stockmanager.dto.stock.response.*;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.service.ChartService;
import com.my.stock.stockmanager.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

	private final StockService service;
	private final ChartService chartService;

	@GetMapping({"/{memberId}", "/{memberId}/{bankId}"})
	public DashboardStockResponse getStocks(@PathVariable String memberId, @PathVariable(required = false) Long bankId) {
		return DashboardStockResponse.builder()
				.stocks(service.getStocks(memberId, bankId))
				.code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage())
				.build();
	}

	@PostMapping
	public BaseResponse saveStock(@RequestBody StockSaveRequest request) throws Exception {
		service.saveStock(request);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@GetMapping("/{memberId}/{national}/{code}")
	public MyDetailStockInfoResponse getMyDetailStock(@PathVariable String memberId, @PathVariable String national, @PathVariable String code, String symbol) throws Exception {
		return MyDetailStockInfoResponse.builder()
				.detail(service.getMyDetailStock(memberId, national, symbol))
				.code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage())
				.build();
	}

	@DeleteMapping("/{id}")
	public BaseResponse deleteStock(@PathVariable Long id) throws StockManagerException {
		service.deleteById(id);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@GetMapping("/chart/{chartType}/{national}/{symbol}")
	public DetailStockChartSeriesResponse getDailyChartData(@PathVariable ChartType chartType, @PathVariable String national, @PathVariable String symbol) throws Exception {
		return DetailStockChartSeriesResponse.builder().chartData(chartService.getDailyChartData(chartType.name(), national, symbol))
				.code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage())
				.build();
	}

	@GetMapping
	public DetailStockInfoResponse getDetailStock(@RequestParam String symbol) throws Exception {
		MyDetailStockInfo info = service.getDetailStock(symbol);
		return new DetailStockInfoResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage(), info, info.getChartData());
	}
}
