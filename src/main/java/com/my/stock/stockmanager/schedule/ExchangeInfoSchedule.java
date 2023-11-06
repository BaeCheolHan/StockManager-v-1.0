package com.my.stock.stockmanager.schedule;

import com.my.stock.stockmanager.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExchangeInfoSchedule {

	private final ExchangeRateService exchangeRateService;

	@Async
	@Scheduled(fixedRate = 1000 * 60 * 30)
	public void scheduleFixedExchangeDollarRateTask() throws IOException {
		exchangeRateService.refresh();
	}


}
