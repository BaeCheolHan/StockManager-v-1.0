package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stocks.response.StocksCodeListResponse;
import com.my.stock.stockmanager.dto.stocks.response.StocksListResponse;
import com.my.stock.stockmanager.dto.stocks.response.StocksAllListResponse;
import com.my.stock.stockmanager.rdb.entity.Stocks;
import com.my.stock.stockmanager.service.StocksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class StocksController {

	private final StocksService stocksService;

	@GetMapping("/stocks/{code}")
	public StocksListResponse getStocksByMarketCode(@PathVariable String code) {
		List<Stocks> list = stocksService.getStocksListByMarketCode(code);
		return StocksListResponse.builder().stocksList(list).code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage()).build();
	}

	@GetMapping("/stocks/code/{national}")
	public StocksCodeListResponse getStockCodes(@PathVariable String national) {
		List<String> list = stocksService.getStocksCodeListByNational(national);
		return StocksCodeListResponse.builder().codes(list).code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage()).build();
	}

	@GetMapping("/stocks/{symbol}")
	public StocksListResponse getStockBySymbol(@PathVariable String symbol) {
		List<Stocks> list = stocksService.getStockBySymbol(symbol);
		return StocksListResponse.builder().stocksList(list).code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage()).build();
	}

	@GetMapping("/stocks/all")
	public StocksAllListResponse getAllStocks() {
		List<Stocks> list = stocksService.getAllStocks();
		return StocksAllListResponse.builder().stocks(list).code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage()).build();
	}

	@GetMapping("/stocks/search")
	public StocksListResponse search(@RequestParam("q") String q, @RequestParam(value = "limit", defaultValue = "50") int limit) {
		List<Stocks> list = stocksService.search(q, Math.max(1, Math.min(limit, 200)));
		return StocksListResponse.builder().stocksList(list).code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage()).build();
	}

	@GetMapping("/stocks/trending")
	public StocksListResponse trending(@RequestParam(value = "limit", defaultValue = "20") int limit) {
		List<Stocks> list = stocksService.trending(limit);
		return StocksListResponse.builder().stocksList(list).code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage()).build();
	}

	@PostMapping("/stocks/search/hit")
	public void increaseSearchHit(@RequestParam("symbol") String symbol) {
		stocksService.increaseSearchHit(symbol);
	}

}
