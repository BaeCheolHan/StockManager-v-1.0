package com.my.stock.stockmanager.redis.data.service;

import com.my.stock.stockmanager.api.kis.KisApi;
import com.my.stock.stockmanager.dto.kis.request.KrStockPriceRequest;
import com.my.stock.stockmanager.dto.kis.response.KrNowStockPriceWrapper;
import com.my.stock.stockmanager.redis.entity.DividendInfo;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import com.my.stock.stockmanager.redis.repository.DividendInfoRepository;
import com.my.stock.stockmanager.redis.repository.KrNowStockPriceRepository;
import com.my.stock.stockmanager.utils.KisApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KrNowStockPriceDataService {
	private final KrNowStockPriceRepository krNowStockPriceRepository;

	private final DividendInfoRepository dividendInfoRepository;

	private final DividendInfoDataService dividendInfoDataService;

	private final KisApi kisApi;

	private final KisApiUtils kisApiUtils;

	public KrNowStockPrice findById(String symbol) throws Exception {

		KrNowStockPrice price = krNowStockPriceRepository.findById(symbol).orElseGet(() -> {
			try {
				HttpHeaders headers = kisApiUtils.getDefaultApiHeader("FHKST01010100");

				KrStockPriceRequest request = KrStockPriceRequest.builder()
						.fid_cond_mrkt_div_code("J")
						.fid_input_iscd(symbol)
						.build();

				KrNowStockPriceWrapper response = kisApi.getKorStockPrice(headers, request);
				response.getOutput().setSymbol(symbol);

				DividendInfo dividendInfo = dividendInfoDataService.findByIdOrElseNew(symbol);

				response.getOutput().setDividendInfo(dividendInfo);
				krNowStockPriceRepository.save(response.getOutput());
				return response.getOutput();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});

		if (price.getDividendInfo() == null) {
			DividendInfo dividendInfo = dividendInfoDataService.findByIdOrElseNew(symbol);
			price.setDividendInfo(dividendInfo);
			krNowStockPriceRepository.save(price);
		}

		return price;
	}
}
