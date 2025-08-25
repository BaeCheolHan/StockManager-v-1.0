package com.my.stock.stockmanager.api.yfin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DividendsDto {
    private String ticker;
    private String range;
    private List<Row> rows;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Row {
        private Instant date;
        private Double amount;
    }
}


