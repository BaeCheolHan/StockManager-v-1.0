package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.api.kis.KisApi;
import com.my.stock.stockmanager.dto.kis.request.KrDailyStockChartPriceRequest;
import com.my.stock.stockmanager.dto.kis.request.OverSeaDailyStockChartPriceRequest;
import com.my.stock.stockmanager.dto.kis.response.KrDailyStockChartPriceOutput2;
import com.my.stock.stockmanager.dto.kis.response.KrDailyStockChartPriceWrapper;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyStockChartPriceOutput2;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyStockChartPriceWrapper;
import com.my.stock.stockmanager.dto.stock.response.DetailStockChartSeries;
import com.my.stock.stockmanager.rdb.data.service.StocksDataService;
import com.my.stock.stockmanager.rdb.entity.Stocks;
import com.my.stock.stockmanager.utils.KisApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChartService {

	private final KisApiUtils kisApiUtils;
	private final StocksDataService stocksDataService;
	private final KisApi kisApi;

	public List<DetailStockChartSeries> getDailyChartData(String chartType, String national, String symbol) throws Exception {
		if (national.equals("KR")) {
			return this.getKrDailyChart(chartType, symbol);
		} else {
			return this.getOverSeaDailyChart(chartType, symbol);
		}
	}

	private List<DetailStockChartSeries> getKrDailyChart(String chartType, String symbol) throws Exception {
		HttpHeaders headers = kisApiUtils.getDefaultApiHeader("FHKST03010100");
		headers.add("custtype", "P");

		KrDailyStockChartPriceRequest request = KrDailyStockChartPriceRequest.builder()
				.FID_COND_MRKT_DIV_CODE("J")
				.FID_INPUT_ISCD(symbol)
				.FID_INPUT_DATE_1(LocalDate.now().minusYears(100L).minusDays(1L).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.FID_INPUT_DATE_2(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.FID_PERIOD_DIV_CODE(chartType)
				.FID_ORG_ADJ_PRC("0")
				.build();
		KrDailyStockChartPriceWrapper resp = kisApi.getKrDailyStockChartPrice(headers, request);

		final DateTimeFormatter dfm = DateTimeFormatter.ofPattern("yyyyMMdd");
		List<KrDailyStockChartPriceOutput2> output2 = resp.getOutput2().stream().filter(it -> it.getStck_bsop_date() != null).sorted(Comparator.comparing(s -> LocalDate.parse(s.getStck_bsop_date(), dfm))).toList();
		resp.setOutput2(output2);

		return resp.getOutput2().stream()
				.filter(it -> it.getStck_bsop_date() != null).map(it -> {
					DetailStockChartSeries series = new DetailStockChartSeries();
					series.setOpen(it.getStck_oprc());
					series.setHigh(it.getStck_hgpr());
					series.setLow(it.getStck_lwpr());
					series.setClose(it.getStck_clpr());
					series.setDate(it.getStck_bsop_date());
					return series;
				}).collect(Collectors.toList());
	}


	private List<DetailStockChartSeries> getOverSeaDailyChart(String chartType, String symbol) throws Exception {
		HttpHeaders headers = kisApiUtils.getDefaultApiHeader("HHDFS76240000");
		headers.add("custtype", "P");
		Stocks stock = stocksDataService.findBySymbol(symbol);

		String gubnValue = "0";

		if (chartType.equals("D")) {
			gubnValue = "0";
		}
		if (chartType.equals("W")) {
			gubnValue = "1";
		}
		if (chartType.equals("M")) {
			gubnValue = "2";
		}

		OverSeaDailyStockChartPriceRequest request = OverSeaDailyStockChartPriceRequest.builder()
				.AUTH("")
				.EXCD(stock.getCode())
				.SYMB(stock.getSymbol())
				.GUBN(gubnValue)
				.BYMD(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.MODP("1")
				.build();

		OverSeaDailyStockChartPriceWrapper resp = kisApi.getOverSeaDailyStockChartPrice(headers, request);

		Collections.reverse(resp.getOutput2());

		final DateTimeFormatter dfm = DateTimeFormatter.ofPattern("yyyyMMdd");
		List<OverSeaDailyStockChartPriceOutput2> output2 = resp.getOutput2().stream().filter(it -> it.getXymd() != null).sorted(Comparator.comparing(s -> LocalDate.parse(s.getXymd(), dfm))).toList();
		resp.setOutput2(output2);

		return resp.getOutput2().stream().filter(it -> it.getXymd() != null).map(it -> {
			DetailStockChartSeries series = new DetailStockChartSeries();
			series.setDate(it.getXymd());
			series.setOpen(it.getOpen());
			series.setHigh(it.getHigh());
			series.setLow(it.getLow());
			series.setClose(it.getClos());
			return series;
		}).collect(Collectors.toList());
	}
}
