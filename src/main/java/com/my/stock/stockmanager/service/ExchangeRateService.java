package com.my.stock.stockmanager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.my.stock.stockmanager.dto.exchangerate.ExchangeRateDto;
import com.my.stock.stockmanager.dto.exchangerate.request.ExchangeRateApiRequest;
import com.my.stock.stockmanager.global.infra.ApiCaller;
import com.my.stock.stockmanager.rdb.entity.ExchangeRate;
import com.my.stock.stockmanager.rdb.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRate getExchangeRate() {
        return exchangeRateRepository.findByDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).orElseGet(() -> {
            ExchangeRate exchangeRate;
            try {
                exchangeRate = this.callExchangeRate();
                exchangeRateRepository.save(exchangeRate);
            } catch (Exception ignore) {
                exchangeRate = new ExchangeRate();
            }

            return exchangeRate;
        });
    }

    @Transactional
    public void refresh() {
        exchangeRateRepository.findByDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .ifPresentOrElse(olderExchangeRate -> {
                            ExchangeRate nowExchangeRate = null;
                            try {
                                nowExchangeRate = this.callExchangeRate();
                            } catch (Exception ignored) {}
                            if(nowExchangeRate != null) {
                                olderExchangeRate.setBasePrice(nowExchangeRate.getBasePrice());
                                olderExchangeRate.setCashBuyingPrice(nowExchangeRate.getCashBuyingPrice());
                                olderExchangeRate.setCashSellingPrice(nowExchangeRate.getCashSellingPrice());
                                olderExchangeRate.setDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
                                olderExchangeRate.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                                exchangeRateRepository.save(olderExchangeRate);
                            }
                        }, () -> {
                            ExchangeRate exchangeRate = null;
                            try {
                                exchangeRate = this.callExchangeRate();
                            } catch (IOException ignored) {} catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            if(exchangeRate != null) {
                                exchangeRateRepository.save(exchangeRate);
                            }
                        });

    }

    public ExchangeRate callExchangeRate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        ExchangeRateApiRequest request = new ExchangeRateApiRequest();
        List<ExchangeRateDto> exchangeRateList = this.callApi(request);
        long minusDays = 1L;
        while(exchangeRateList.isEmpty()) {
            exchangeRateList = this.callApi(request.minusDay(minusDays));
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

    private List<ExchangeRateDto> callApi(ExchangeRateApiRequest request) throws Exception {
        String responseBody  = ApiCaller.getInstance().get("https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?" + request.getUriQuery());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody, new TypeReference<>() {});
    }
}
