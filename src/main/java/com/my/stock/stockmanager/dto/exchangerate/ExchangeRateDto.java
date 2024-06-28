package com.my.stock.stockmanager.dto.exchangerate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.my.stock.stockmanager.config.jackson.BigDecimalDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExchangeRateDto {
    private String result;
    private String cur_unit;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal tts;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal ttb;
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal deal_bas_r;
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal bkpr;
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal yy_efee_r;
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal ten_dd_efee_r;
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal kftc_bkpr;
    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal kftc_deal_bas_r;
    private String cur_nm;
}
