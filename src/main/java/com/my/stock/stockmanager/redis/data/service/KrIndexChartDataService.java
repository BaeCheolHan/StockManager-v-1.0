package com.my.stock.stockmanager.redis.data.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.redis.entity.KrIndexChart;
import com.my.stock.stockmanager.redis.repository.KrIndexChartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KrIndexChartDataService {
	private final KrIndexChartRepository krIndexChartRepository;

	public KrIndexChart findByCodeAndChartTypeOrElseThrow(String id, String chartType) throws StockManagerException {
		return krIndexChartRepository.findByCodeAndChartType(id, chartType).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
	}

	public KrIndexChart save(KrIndexChart chart) {
		return krIndexChartRepository.save(chart);
	}
}
