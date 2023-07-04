package com.my.stock.stockmanager.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.my.stock.stockmanager.global.infra.ApiCaller;
import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import com.my.stock.stockmanager.rdb.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeInfoSchedule {

	@Value("${api.exchage-rate-api-url}")
	private String exchangeRateApiUrl;
	private final ExchangeRateRepository exchangeRateRepository;

	@Async
	@Scheduled(fixedRate = 1000 * 10 * 30)
	public void scheduleFixedExchangeDollarRateTask() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		HashMap<String, Object> param = new HashMap<>();
		param.put("codes", "FRX.KRWUSD");
		List<ExchangeRate> exchangeRateList = Arrays.asList(new ObjectMapper()
				.readValue(ApiCaller.getInstance().get(exchangeRateApiUrl, param), ExchangeRate[].class));

		exchangeRateList.forEach(exchangeRate -> {
			exchangeRateRepository.deleteById(exchangeRate.getId());
			exchangeRateRepository.save(exchangeRate);
		});

	}
}
