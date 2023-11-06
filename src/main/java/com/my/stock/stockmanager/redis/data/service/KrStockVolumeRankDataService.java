package com.my.stock.stockmanager.redis.data.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.redis.entity.KrIndexChart;
import com.my.stock.stockmanager.redis.entity.KrStockVolumeRank;
import com.my.stock.stockmanager.redis.repository.KrStockVolumeRankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KrStockVolumeRankDataService {
	private final KrStockVolumeRankRepository krIndexChartRepository;

	public KrStockVolumeRank findByIdOrElseThrow(String id) throws StockManagerException {
		return krIndexChartRepository.findById(id).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
	}

	public KrStockVolumeRank save(KrStockVolumeRank entity) {
		return krIndexChartRepository.save(entity);
	}
}
