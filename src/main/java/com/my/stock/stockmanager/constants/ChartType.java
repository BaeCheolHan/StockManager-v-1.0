package com.my.stock.stockmanager.constants;

import java.util.Locale;

public enum ChartType {
    D("0"),
    W("1"),
    M("2");

    private final String kisGubn;

    ChartType(String kisGubn) {
        this.kisGubn = kisGubn;
    }

    public String getKisGubn() {
        return kisGubn;
    }

    public static ChartType from(String code) {
        if (code == null) return D;
        try {
            return ChartType.valueOf(code.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ignore) {
            return D;
        }
    }
}


