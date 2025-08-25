package com.my.stock.stockmanager.constants;

public enum Currency {
    KRW,
    USD,
    JPY,
    HKD,
    CNY,
    VND,
    OTHER;

    public static Currency from(String code) {
        if (code == null) return OTHER;
        try {
            return Currency.valueOf(code);
        } catch (IllegalArgumentException ex) {
            return OTHER;
        }
    }
}


