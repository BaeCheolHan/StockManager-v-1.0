package com.my.stock.stockmanager.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Money {
    private static final int SCALE_INTERMEDIATE = 4;
    private static final int SCALE_PERCENT = 2;
    private static final RoundingMode ROUND_INTERMEDIATE = RoundingMode.DOWN;
    private static final RoundingMode ROUND_PERCENT = RoundingMode.FLOOR;

    private Money() {}

    public static String percentToString(BigDecimal change, BigDecimal base) {
        if (change == null || base == null || base.compareTo(BigDecimal.ZERO) == 0) {
            return "0.00";
        }
        BigDecimal ratio = change.divide(base, SCALE_INTERMEDIATE, ROUND_INTERMEDIATE);
        BigDecimal percent = ratio.multiply(BigDecimal.valueOf(100));
        return percent.setScale(SCALE_PERCENT, ROUND_PERCENT).toString();
    }

    public static BigDecimal percent(BigDecimal value, BigDecimal total) {
        if (value == null || total == null || total.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(SCALE_PERCENT, ROUND_PERCENT);
        }
        BigDecimal ratio = value.divide(total, SCALE_INTERMEDIATE, ROUND_INTERMEDIATE);
        return ratio.multiply(BigDecimal.valueOf(100)).setScale(SCALE_PERCENT, ROUND_PERCENT);
    }
}


