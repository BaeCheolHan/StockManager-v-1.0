package com.my.stock.stockmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.my.stock.stockmanager.api.exchagerate.ExchangeRateApi;
import com.my.stock.stockmanager.dto.exchangerate.ExchangeRateDto;
import com.my.stock.stockmanager.dto.exchangerate.request.ExchangeRateApiRequest;
import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import com.my.stock.stockmanager.rdb.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    private final ExchangeRateApi exchangeRateApi;

    public ExchangeRate getExchangeRate() {
        return exchangeRateRepository.findByDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).orElseGet(() -> {
            ExchangeRate exchangeRate = this.callExchangeRate();
            exchangeRateRepository.save(exchangeRate);
            return exchangeRate;
        });
    }

    public ExchangeRate callExchangeRate() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        ExchangeRateApiRequest request = new ExchangeRateApiRequest();
        List<ExchangeRateDto> exchangeRateList = exchangeRateApi.getExchangeRate(request);
        long minusDays = 1L;
        while(exchangeRateList.isEmpty()) {
            exchangeRateList = exchangeRateApi.getExchangeRate(request.minusDay(minusDays));
            minusDays++;
        }
        return exchangeRateList.stream()
                .filter(it -> it.getCur_unit().equals("USD"))
                .map(it -> {
                    ExchangeRate rate = new ExchangeRate();
                    rate.setBasePrice(it.getBkpr());
                    rate.setCashBuyingPrice(it.getTts());
                    rate.setCashSellingPrice(it.getTtb());
                    rate.setDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
                    rate.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                    return rate;
                }).findFirst().get();
    }

    @Transactional
    public void refresh() {
        exchangeRateRepository.findByDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .ifPresentOrElse(olderExchangeRate -> {
                            ExchangeRate nowExchangeRate = this.callExchangeRate();
                            olderExchangeRate.setBasePrice(nowExchangeRate.getBasePrice());
                            olderExchangeRate.setCashBuyingPrice(nowExchangeRate.getCashBuyingPrice());
                            olderExchangeRate.setCashSellingPrice(nowExchangeRate.getCashSellingPrice());
                            olderExchangeRate.setDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
                            olderExchangeRate.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                            exchangeRateRepository.save(olderExchangeRate);
                        }, () -> {
                            ExchangeRate exchangeRate = this.callExchangeRate();
                            exchangeRateRepository.save(exchangeRate);
                        });

    }
}
