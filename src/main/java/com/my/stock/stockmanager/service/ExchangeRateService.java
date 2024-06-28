package com.my.stock.stockmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.my.stock.stockmanager.dto.exchangerate.ExchangeRateDto;
import com.my.stock.stockmanager.global.infra.ApiCaller;
import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import com.my.stock.stockmanager.rdb.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    @Value("${api.exchage-rate-api-url}")
    private String exchangeRateApiUrl;

    @Value("${api.ecos.key}")
    private String ecosApiKey;
    private final ExchangeRateRepository exchangeRateRepository;

    private final String API_KEY = "wi0EWWwKJgqc97jJJIdnXRn8YBiACxqo";
    private final String API_DATA_TYPE = "AP01";
    private final String API_URL = String.format("https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=%s&searchdate=%s&data=%s"
            , API_KEY, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), API_DATA_TYPE);

    public ExchangeRate getExchangeRate() throws IOException {
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();

        if (!exchangeRateList.isEmpty()) {
            return exchangeRateList.get(exchangeRateList.size() - 1);
        } else {

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            return Arrays.stream(new ObjectMapper()
                            .readValue(ApiCaller.getInstance().get(API_URL), ExchangeRateDto[].class)).filter(it -> it.getCur_unit().equals("USD"))
                    .map(it -> {
                        exchangeRateRepository.deleteAll();
                        ExchangeRate exchangeRate = new ExchangeRate();
                        exchangeRate.setBasePrice(it.getBkpr());
                        exchangeRate.setCashBuyingPrice(it.getTts());
                        exchangeRate.setCashSellingPrice(it.getTtb());
                        exchangeRate.setDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
                        exchangeRate.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                        exchangeRateRepository.save(exchangeRate);
                        return exchangeRate;
                    }).findFirst().get();
        }

    }

    public void refresh() throws IOException {
        exchangeRateRepository.deleteAll();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Arrays.stream(new ObjectMapper()
                .readValue(ApiCaller.getInstance().get(API_URL), ExchangeRateDto[].class)).filter(it -> it.getCur_unit().equals("USD")).forEach(it -> {
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
