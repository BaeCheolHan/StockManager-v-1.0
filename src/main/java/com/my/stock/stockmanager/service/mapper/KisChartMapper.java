package com.my.stock.stockmanager.service.mapper;

import com.my.stock.stockmanager.dto.kis.response.KrDailyIndexChartPriceOutput2;
import com.my.stock.stockmanager.dto.kis.response.KrDailyStockChartPriceOutput2;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyIndexChartPriceOutput2;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyStockChartPriceOutput2;
import com.my.stock.stockmanager.dto.stock.response.DetailStockChartSeries;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public final class KisChartMapper {
    private KisChartMapper() {}

    private static final DateTimeFormatter KR_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static List<DetailStockChartSeries> mapKrDailyStock(List<KrDailyStockChartPriceOutput2> list) {
        return list.stream()
                .filter(it -> it.getStck_bsop_date() != null)
                .sorted(java.util.Comparator.comparing(s -> LocalDate.parse(s.getStck_bsop_date(), KR_FMT)))
                .map(it -> {
                    DetailStockChartSeries series = new DetailStockChartSeries();
                    series.setOpen(it.getStck_oprc());
                    series.setHigh(it.getStck_hgpr());
                    series.setLow(it.getStck_lwpr());
                    series.setClose(it.getStck_clpr());
                    series.setDate(it.getStck_bsop_date());
                    return series;
                }).collect(Collectors.toList());
    }

    public static List<DetailStockChartSeries> mapOverseaDailyStock(List<OverSeaDailyStockChartPriceOutput2> list) {
        return list.stream()
                .filter(it -> it.getXymd() != null)
                .sorted(java.util.Comparator.comparing(s -> LocalDate.parse(s.getXymd(), KR_FMT)))
                .map(it -> {
                    DetailStockChartSeries series = new DetailStockChartSeries();
                    series.setDate(it.getXymd());
                    series.setOpen(it.getOpen());
                    series.setHigh(it.getHigh());
                    series.setLow(it.getLow());
                    series.setClose(it.getClos());
                    return series;
                }).collect(Collectors.toList());
    }

    public static List<KrDailyIndexChartPriceOutput2> sortKrIndex(List<KrDailyIndexChartPriceOutput2> list) {
        return list.stream()
                .sorted(java.util.Comparator.comparing(s -> LocalDate.parse(s.getStck_bsop_date(), KR_FMT)))
                .collect(Collectors.toList());
    }

    public static List<OverSeaDailyIndexChartPriceOutput2> sortOverseaIndex(List<OverSeaDailyIndexChartPriceOutput2> list) {
        return list.stream()
                .sorted(java.util.Comparator.comparing(s -> LocalDate.parse(s.getStck_bsop_date(), KR_FMT)))
                .collect(Collectors.toList());
    }
}


