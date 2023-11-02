package com.my.stock.stockmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.my.stock.stockmanager.global.infra.ApiCaller;
import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import com.my.stock.stockmanager.rdb.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
	@Value("${api.exchage-rate-api-url}")
	private String exchangeRateApiUrl;
	private final ExchangeRateRepository exchangeRateRepository;

	public ExchangeRate getExchangeRate() throws IOException {
		List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();

		if (!exchangeRateList.isEmpty()) {
			return exchangeRateList.get(exchangeRateList.size() - 1);
		} else {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			HashMap<String, Object> param = new HashMap<>();
			param.put("codes", "FRX.KRWUSD");
			List<ExchangeRate> list = Arrays.asList(new ObjectMapper()
					.readValue(ApiCaller.getInstance().get(exchangeRateApiUrl, param), ExchangeRate[].class));

			list.forEach(exchangeRate -> {
				exchangeRateRepository.deleteById(exchangeRate.getId());
				exchangeRateRepository.save(exchangeRate);
			});

			return list.get(list.size() - 1);
		}

	}
}
