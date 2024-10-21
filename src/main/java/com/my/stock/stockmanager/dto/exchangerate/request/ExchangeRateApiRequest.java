package com.my.stock.stockmanager.dto.exchangerate.request;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ExchangeRateApiRequest {
    private String data = "AP01";

    private String authkey = "wi0EWWwKJgqc97jJJIdnXRn8YBiACxqo";

    private String searchdate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));


    public ExchangeRateApiRequest minusDay(long days) {
        this.searchdate = LocalDateTime.now().minusDays(days).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return this;
    }

    public String getUriQuery() {
        return String.format("data=%s&authkey=%s&searchdate=%s", data, authkey, searchdate);
    }
}
