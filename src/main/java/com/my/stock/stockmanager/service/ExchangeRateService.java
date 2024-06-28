package com.my.stock.stockmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.my.stock.stockmanager.api.exchagerate.ExchangeRateApi;
import com.my.stock.stockmanager.dto.exchangerate.request.ExchangeRateApiRequest;
import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import com.my.stock.stockmanager.rdb.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    private final ExchangeRateApi exchangeRateApi;

    public ExchangeRate getExchangeRate() {
        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findFirstByOrderByIdDesc();

        return exchangeRate.orElseGet(() -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            return exchangeRateApi.getExchangeRate(new ExchangeRateApiRequest()).stream()
                    .filter(it -> it.getCur_unit().equals("USD"))
                    .map(it -> {
                        ExchangeRate rate = new ExchangeRate();
                        rate.setBasePrice(it.getBkpr());
                        rate.setCashBuyingPrice(it.getTts());
                        rate.setCashSellingPrice(it.getTtb());
                        rate.setDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
                        rate.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                        exchangeRateRepository.save(rate);
                        return rate;
                    }).findFirst().get();
        });
    }

    public void refresh() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        exchangeRateApi.getExchangeRate(new ExchangeRateApiRequest()).stream().filter(it -> it.getCur_unit().equals("USD"))
                .forEach(it -> {
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setBasePrice(it.getBkpr());
                    exchangeRate.setCashBuyingPrice(it.getTts());
                    exchangeRate.setCashSellingPrice(it.getTtb());
                    exchangeRate.setDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
                    exchangeRate.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                    exchangeRateRepository.save(exchangeRate);
                });

    }
}
