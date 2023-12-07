package com.my.stock.stockmanager.redis.data.service;

import com.my.stock.stockmanager.global.infra.ApiCaller;
import com.my.stock.stockmanager.redis.entity.DividendInfo;
import com.my.stock.stockmanager.redis.repository.DividendInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DividendInfoDataService {

	private final DividendInfoRepository dividendInfoRepository;

	public DividendInfo findByIdOrElseNew(String id) {
		return dividendInfoRepository.findById(id).orElseGet(() -> {
			Map<String, Object> param = new HashMap<>();
			param.put("symbol", id);
			try {
				ApiCaller.getInstance().get("http://127.0.0.1:18082/batch/run/DividendDataSaveJob", param);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return dividendInfoRepository.findById(id).orElseGet(DividendInfo::new);
		});
	}
}
