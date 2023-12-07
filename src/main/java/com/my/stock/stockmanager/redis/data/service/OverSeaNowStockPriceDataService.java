package com.my.stock.stockmanager.redis.data.service;

import com.my.stock.stockmanager.api.kis.KisApi;
import com.my.stock.stockmanager.dto.kis.request.OverSeaStockPriceRequest;
import com.my.stock.stockmanager.dto.kis.response.OverSeaNowStockPriceWrapper;
import com.my.stock.stockmanager.global.infra.ApiCaller;
import com.my.stock.stockmanager.rdb.data.service.StocksDataService;
import com.my.stock.stockmanager.rdb.entity.Stocks;
import com.my.stock.stockmanager.redis.entity.DividendInfo;
import com.my.stock.stockmanager.redis.entity.OverSeaNowStockPrice;
import com.my.stock.stockmanager.redis.repository.DividendInfoRepository;
import com.my.stock.stockmanager.redis.repository.OverSeaNowStockPriceRepository;
import com.my.stock.stockmanager.utils.KisApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OverSeaNowStockPriceDataService {
	private final OverSeaNowStockPriceRepository overSeaNowStockPriceRepository;
	private final DividendInfoDataService dividendInfoDataService;

	private final StocksDataService stocksDataService;
	private final KisApi kisApi;

	private final KisApiUtils kisApiUtils;

	public OverSeaNowStockPrice findById(String symbol) {

		return overSeaNowStockPriceRepository.findById(symbol).orElseGet(() -> {
			try {
				Stocks stocks = stocksDataService.findBySymbol(symbol);
				HttpHeaders headers = kisApiUtils.getDefaultApiHeader("HHDFS76200200");
				headers.add("custtype", "P");

				OverSeaNowStockPriceWrapper response = kisApi.getOverSeaStockPrice(headers, OverSeaStockPriceRequest.builder()
						.AUTH("")
						.EXCD(stocks.getCode())
						.SYMB(stocks.getSymbol())
						.build());
				response.getOutput().setSymbol(symbol);

				DividendInfo dividendInfo = dividendInfoDataService.findByIdOrElseNew(symbol);

				response.getOutput().setDividendInfo(dividendInfo);

				overSeaNowStockPriceRepository.save(response.getOutput());
				return response.getOutput();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		});
	}
}
