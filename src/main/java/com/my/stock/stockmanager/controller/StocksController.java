package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.stocks.response.StocksCodeListResponse;
import com.my.stock.stockmanager.dto.stocks.response.StocksListResponse;
import com.my.stock.stockmanager.rdb.entity.Stocks;
import com.my.stock.stockmanager.rdb.repository.StocksRepository;
import com.my.stock.stockmanager.service.StocksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class StocksController {

	private final StocksService stocksService;

	@GetMapping("/stocks/{code}")
	public StocksListResponse getStocks(@PathVariable String code) {
		List<Stocks> list = stocksService.getStocksListByCode(code);
		return StocksListResponse.builder().stocksList(list).code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage()).build();
	}

	@GetMapping("/stocks/code/{national}")
	public StocksCodeListResponse getStockCodes(@PathVariable String national) {
		List<String> list = stocksService.getStocksCodeListByNational(national);
		return StocksCodeListResponse.builder().codes(list).code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage()).build();
	}

}
