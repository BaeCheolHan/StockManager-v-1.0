package com.my.stock.stockmanager.redis.entity;

import java.math.BigDecimal;

public interface NowStockPrice {
    BigDecimal nowPrice();
    BigDecimal compareToYesterday();
}


