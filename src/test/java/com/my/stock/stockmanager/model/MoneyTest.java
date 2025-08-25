package com.my.stock.stockmanager.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {
    @Test
    void percentToString_basic() {
        String pct = Money.percentToString(new BigDecimal("10"), new BigDecimal("100"));
        assertEquals("10.00", pct);
    }

    @Test
    void percent_basic() {
        BigDecimal pct = Money.percent(new BigDecimal("50"), new BigDecimal("200"));
        assertEquals(new BigDecimal("25.00"), pct);
    }
}


