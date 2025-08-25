package com.my.stock.stockmanager.api.yfin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteDto {
    private String symbol;
    private String shortName;
    private String currency;
    private Double forwardDividendYieldPct;
}


