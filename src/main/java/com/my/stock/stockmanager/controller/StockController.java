package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stock.request.StockSaveRequest;
import com.my.stock.stockmanager.dto.stock.response.DashboardStockResponse;
import com.my.stock.stockmanager.dto.stock.response.DetailStockChartSeriesResponse;
import com.my.stock.stockmanager.dto.stock.response.DetailStockInfoResponse;
import com.my.stock.stockmanager.dto.stock.response.MyDetailStockInfoResponse;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

	private final StockService service;

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
				.detail(service.getMyDetailStock(memberId, national, code, symbol))
				.code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage())
				.build();
	}

	@DeleteMapping("/{id}")
	public BaseResponse deleteStock(@PathVariable Long id) throws StockManagerException {
		service.deleteById(id);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@GetMapping("/chart/{chartType}/{national}/{symbol}")
	public DetailStockChartSeriesResponse getDailyChartData(@PathVariable String chartType, @PathVariable String national, @PathVariable String symbol) throws Exception {
		return DetailStockChartSeriesResponse.builder().chartData(service.getDailyChartData(chartType, national, symbol))
				.code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage())
				.build();
	}

	@GetMapping
	public DetailStockInfoResponse getDetailStock(@RequestParam String symbol) throws Exception {
		return service.getDetailStock(symbol);
	}
}
