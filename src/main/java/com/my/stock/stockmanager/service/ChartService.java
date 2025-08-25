package com.my.stock.stockmanager.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import static com.my.stock.stockmanager.constants.CacheNames.CHART_SERIES;

import com.my.stock.stockmanager.api.kis.KisApi;
import com.my.stock.stockmanager.constants.ChartType;
import com.my.stock.stockmanager.dto.kis.request.KrDailyStockChartPriceRequest;
import com.my.stock.stockmanager.dto.kis.request.OverSeaDailyStockChartPriceRequest;
import com.my.stock.stockmanager.dto.kis.response.KrDailyStockChartPriceWrapper;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyStockChartPriceWrapper;
import com.my.stock.stockmanager.dto.stock.response.DetailStockChartSeries;
import com.my.stock.stockmanager.rdb.data.service.StocksDataService;
import com.my.stock.stockmanager.rdb.entity.Stocks;
import com.my.stock.stockmanager.service.mapper.KisChartMapper;
import com.my.stock.stockmanager.utils.KisApiUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChartService {

	private final KisApiUtils kisApiUtils;
	private final StocksDataService stocksDataService;
	private final KisApi kisApi;

	@Cacheable(cacheNames = CHART_SERIES, key = "#chartType + ':' + #national + ':' + #symbol")
	public List<DetailStockChartSeries> getDailyChartData(String chartType, String national, String symbol) throws Exception {
		ChartType ct = ChartType.from(chartType);
		if (com.my.stock.stockmanager.constants.National.from(national).isKr()) {
			return this.getKrDailyChart(ct, symbol);
		} else {
			return this.getOverSeaDailyChart(ct, symbol);
		}
	}

	private List<DetailStockChartSeries> getKrDailyChart(ChartType chartType, String symbol) throws Exception {
		HttpHeaders headers = kisApiUtils.getDefaultApiHeader("FHKST03010100");
		headers.add("custtype", "P");

		KrDailyStockChartPriceRequest request = KrDailyStockChartPriceRequest.builder()
				.FID_COND_MRKT_DIV_CODE("J")
				.FID_INPUT_ISCD(symbol)
				.FID_INPUT_DATE_1(LocalDate.now().minusYears(100L).minusDays(1L).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.FID_INPUT_DATE_2(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.FID_PERIOD_DIV_CODE(chartType.name())
				.FID_ORG_ADJ_PRC("0")
				.build();
		KrDailyStockChartPriceWrapper resp = kisApi.getKrDailyStockChartPrice(headers, request);

		return KisChartMapper.mapKrDailyStock(resp.getOutput2());
	}


	private List<DetailStockChartSeries> getOverSeaDailyChart(ChartType chartType, String symbol) throws Exception {
		HttpHeaders headers = kisApiUtils.getDefaultApiHeader("HHDFS76240000");
		headers.add("custtype", "P");
		Stocks stock = stocksDataService.findBySymbol(symbol);

		String gubnValue = chartType.getKisGubn();

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

		return KisChartMapper.mapOverseaDailyStock(resp.getOutput2());
	}
}
