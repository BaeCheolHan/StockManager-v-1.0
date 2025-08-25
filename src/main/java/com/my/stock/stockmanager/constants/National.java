package com.my.stock.stockmanager.constants;

public enum National {
    KR,
    US,
    JP,
    HK,
    CN,
    VN,
    OTHER;

    public static National from(String code) {
        if (code == null) return OTHER;
        try {
            return National.valueOf(code);
        } catch (IllegalArgumentException ex) {
            return OTHER;
        }
    }

    public boolean isKr() {
        return this == KR;
    }
}


