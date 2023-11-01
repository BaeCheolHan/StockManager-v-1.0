package com.my.stock.stockmanager.redis.data.service;

import com.my.stock.stockmanager.api.kis.KisApi;
import com.my.stock.stockmanager.dto.kis.request.KrStockPriceRequest;
import com.my.stock.stockmanager.dto.kis.response.KrNowStockPriceWrapper;
import com.my.stock.stockmanager.redis.entity.KrNowStockPrice;
import com.my.stock.stockmanager.redis.repository.KrNowStockPriceRepository;
import com.my.stock.stockmanager.utils.KisApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KrNowStockPriceDataService {
	private final KrNowStockPriceRepository krNowStockPriceRepository;

	private final KisApi kisApi;

	private final KisApiUtils kisApiUtils;

	public KrNowStockPrice findById(String symbol) {

		return krNowStockPriceRepository.findById(symbol).orElseGet(() -> {
			try {
				HttpHeaders headers = kisApiUtils.getDefaultApiHeader();
				headers.add("tr_id", "FHKST01010100");

				KrStockPriceRequest request = KrStockPriceRequest.builder()
						.fid_cond_mrkt_div_code("J")
						.fid_input_iscd(symbol)
						.build();

				KrNowStockPriceWrapper response = kisApi.getKorStockPrice(headers, request);
				response.getOutput().setSymbol(symbol);
				krNowStockPriceRepository.save(response.getOutput());
				return response.getOutput();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		});
	}
}
