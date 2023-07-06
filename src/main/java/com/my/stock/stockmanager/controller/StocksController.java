package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
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
	private final StocksRepository repository;

	private final StocksService stocksService;

	@GetMapping("/stocks/{national}")
	public StocksListResponse getStocks(@PathVariable String national) {
		List<Stocks> list = stocksService.getStocksListEitherKrAndOverSea(national);
		return StocksListResponse.builder().stocksList(list).code(ResponseCode.SUCCESS).message(ResponseCode.SUCCESS.getMessage()).build();
	}

	@PostMapping("/excel/oversea")
	public BaseResponse saveOverSeaStocks() {
		Workbook workbook = null;
		try {
			File file = ResourceUtils.getFile("classpath:stock-list/overseas_stock_code(all).xlsx");
			InputStream in = new FileInputStream(file);

			workbook = new XSSFWorkbook(in);
			Sheet sheet = workbook.getSheetAt(0);
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				Stocks stocks = new Stocks();
				stocks.setNational(String.valueOf(sheet.getRow(i).getCell(0)));
				stocks.setSymbol(String.valueOf(sheet.getRow(i).getCell(4)));
				stocks.setCode(String.valueOf(sheet.getRow(i).getCell(2)));
				stocks.setName(String.valueOf(sheet.getRow(i).getCell(6)));
				stocks.setCurrency(String.valueOf(sheet.getRow(i).getCell(9)));
				repository.save(stocks);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@PostMapping("/excel/kospi")
	public BaseResponse saveKospi() {
		Workbook workbook = null;
		try {
			File file = ResourceUtils.getFile("classpath:stock-list/kospi_code.xlsx");
			InputStream in = new FileInputStream(file);

			workbook = new XSSFWorkbook(in);
			Sheet sheet = workbook.getSheetAt(0);
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				Stocks stocks = new Stocks();
				stocks.setNational("KR");
				stocks.setSymbol(String.valueOf(sheet.getRow(i).getCell(0)));
				stocks.setCode("KOSPI");
				stocks.setName(String.valueOf(sheet.getRow(i).getCell(2)));
				stocks.setCurrency("KRW");
				repository.save(stocks);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@PostMapping("/excel/kosdaq")
	public BaseResponse saveKosdaq() {
		Workbook workbook = null;
		try {
			File file = ResourceUtils.getFile("classpath:stock-list/ff_code.xlsx");
			InputStream in = new FileInputStream(file);

			workbook = new XSSFWorkbook(in);
			Sheet sheet = workbook.getSheetAt(0);
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				Stocks stocks = new Stocks();
				stocks.setNational("KR");
				stocks.setSymbol(String.valueOf(sheet.getRow(i).getCell(0)));
				stocks.setCode("KOSDAQ");
				stocks.setName(String.valueOf(sheet.getRow(i).getCell(2)));
				stocks.setCurrency("KRW");
				repository.save(stocks);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}
}
