package com.my.stock.stockmanager.dto.stock;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class DashboardStockTest {
    @Test
    void calculateRateOfReturn_basic() {
        DashboardStock s = DashboardStock.builder()
                .avgPrice(new BigDecimal("100"))
                .nowPrice(new BigDecimal("110"))
                .build();
        s.calculateRateOfReturn();
        assertEquals("10.00", s.getRateOfReturnPer());
    }

    @Test
    void calculatePriceImportance_kr_and_oversea() {
        // KR
        DashboardStock kr = DashboardStock.builder()
                .national("KR")
                .nowPrice(new BigDecimal("100"))
                .quantity(new BigDecimal("2"))
                .build();
        kr.calculatePriceImportance(new BigDecimal("400"), null);
        assertEquals(new BigDecimal("50.00"), kr.getPriceImportance());

        // Overseas
        DashboardStock us = DashboardStock.builder()
                .national("US")
                .nowPrice(new BigDecimal("10"))
                .quantity(new BigDecimal("10"))
                .build();
        us.calculatePriceImportance(new BigDecimal("4000"), new BigDecimal("10"));
        assertEquals(new BigDecimal("25.00"), us.getPriceImportance());
    }
}


