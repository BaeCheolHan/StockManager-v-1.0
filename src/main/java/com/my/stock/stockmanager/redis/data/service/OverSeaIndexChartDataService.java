package com.my.stock.stockmanager.redis.data.service;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.redis.entity.OverSeaIndexChart;
import com.my.stock.stockmanager.redis.repository.OverSeaIndexChartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OverSeaIndexChartDataService {
	private final OverSeaIndexChartRepository overSeaIndexChartRepository;

	public OverSeaIndexChart findByIdOrElseThrow(String id) throws StockManagerException {
		return overSeaIndexChartRepository.findById(id).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
	}

	public OverSeaIndexChart save(OverSeaIndexChart chart) {
		return overSeaIndexChartRepository.save(chart);
	}

	public OverSeaIndexChart findByCodeAndChartTypeOrElseThrow(String id, String chartType) throws StockManagerException {
		return overSeaIndexChartRepository.findByCodeAndChartType(id, chartType).orElseThrow(() -> new StockManagerException(ResponseCode.NOT_FOUND_ID));
	}
}
