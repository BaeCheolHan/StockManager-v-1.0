package com.my.stock.stockmanager.redis.data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.my.stock.stockmanager.api.kis.KisApi;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.kis.request.KrStockVolumeRankRequest;
import com.my.stock.stockmanager.dto.kis.response.KrStockVolumeRankWrapper;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.redis.entity.KrStockVolumeRank;
import com.my.stock.stockmanager.redis.repository.KrStockVolumeRankRepository;
import com.my.stock.stockmanager.utils.KisApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Service
@RequiredArgsConstructor
public class KrStockVolumeRankDataService {
	private final KisApiUtils kisApiUtils;
	private final KisApi kisApi;
	private final KrStockVolumeRankRepository krIndexChartRepository;

	public KrStockVolumeRank findByIdOrElseThrow(String id) throws StockManagerException {
		return krIndexChartRepository.findById(id).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
	}

	public KrStockVolumeRank findById(String id) {
		return krIndexChartRepository.findById(id).orElseGet(() -> {
			HttpHeaders headers;
			try {
				headers = kisApiUtils.getDefaultApiHeader("FHPST01710000");
			} catch (MalformedURLException | JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			KrStockVolumeRankRequest request = KrStockVolumeRankRequest.builder()
					.FID_COND_MRKT_DIV_CODE("J")
					.FID_COND_SCR_DIV_CODE("20171")
					.FID_INPUT_ISCD(id)
					.FID_DIV_CLS_CODE("0")
					.FID_BLNG_CLS_CODE("0")
					.FID_TRGT_CLS_CODE("111111111")
					.FID_TRGT_EXLS_CLS_CODE("000000")
					.FID_INPUT_PRICE_1("0")
					.FID_INPUT_PRICE_2("0")
					.FID_VOL_CNT("0")
					.FID_INPUT_DATE_1("0")
					.build();
			KrStockVolumeRankWrapper resp = kisApi.getStockVolumeRank(headers, request);
			KrStockVolumeRank entity = new KrStockVolumeRank();
			entity.setId("0000");
			entity.setData(resp);

			krIndexChartRepository.save(entity);
			return entity;
		});
	}

	public KrStockVolumeRank save(KrStockVolumeRank entity) {
		return krIndexChartRepository.save(entity);
	}
}
