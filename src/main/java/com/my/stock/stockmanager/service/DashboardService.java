package com.my.stock.stockmanager.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import static com.my.stock.stockmanager.constants.CacheNames.DASHBOARD_INDEX;

import com.my.stock.stockmanager.api.kis.KisApi;
import com.my.stock.stockmanager.constants.ChartType;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.dashboard.index.response.IndexChartResponse;
import com.my.stock.stockmanager.dto.kis.request.KrDailyIndexChartPriceRequest;
import com.my.stock.stockmanager.dto.kis.request.OverSeaDailyIndexChartPriceRequest;
import com.my.stock.stockmanager.dto.kis.response.KrDailyIndexChartPriceWrapper;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyIndexChartPriceWrapper;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.redis.data.service.KrIndexChartDataService;
import com.my.stock.stockmanager.redis.data.service.KrStockVolumeRankDataService;
import com.my.stock.stockmanager.redis.data.service.OverSeaIndexChartDataService;
import com.my.stock.stockmanager.redis.entity.KrIndexChart;
import com.my.stock.stockmanager.redis.entity.KrStockVolumeRank;
import com.my.stock.stockmanager.redis.entity.OverSeaIndexChart;
import com.my.stock.stockmanager.service.mapper.KisChartMapper;
import com.my.stock.stockmanager.utils.KisApiUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final KisApi kisApi;
	private final KisApiUtils kisApiUtils;
	private final KrIndexChartDataService krIndexChartDataService;
	private final OverSeaIndexChartDataService overSeaIndexChartDataService;

	private final KrStockVolumeRankDataService krStockVolumeRankDataService;


	@Cacheable(cacheNames = DASHBOARD_INDEX, key = "#chartType")
	public IndexChartResponse getIndexChart(String chartType) throws Exception {
		ChartType ct = ChartType.from(chartType);
		KrIndexChart kospi = this.getKrIndexChart("kospi", "0001", ct);
		KrIndexChart kosdaq = this.getKrIndexChart("kosdaq", "1001", ct);

		OverSeaIndexChart snp = this.getOverSeaIndexChart("snp", "SPX", ct);
		OverSeaIndexChart nasdaq = this.getOverSeaIndexChart("nasdaq", "COMP", ct);
		OverSeaIndexChart daw = this.getOverSeaIndexChart("daw", ".DJI", ct);
		OverSeaIndexChart philadelphia = this.getOverSeaIndexChart("philadelphia", "SOX", ct);
		return new IndexChartResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage(), kospi.getData(), kosdaq.getData(), snp.getData(), nasdaq.getData(), daw.getData(), philadelphia.getData());
	}

	private OverSeaIndexChart getOverSeaIndexChart(String id, String code, ChartType chartType) throws Exception {
		OverSeaIndexChart chart;
		try {
			chart = overSeaIndexChartDataService.findByCodeAndChartTypeOrElseThrow(id, chartType.name());
		} catch (StockManagerException e) {
			HttpHeaders headers;
			headers = kisApiUtils.getDefaultApiHeader("FHKST03030100");

			OverSeaDailyIndexChartPriceRequest request = OverSeaDailyIndexChartPriceRequest.builder()
					.FID_COND_MRKT_DIV_CODE("N")
					.FID_INPUT_DATE_1(LocalDate.now().minusYears(100L).minusDays(1L).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
					.FID_INPUT_DATE_2(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
					.FID_INPUT_ISCD(code)
					.FID_PERIOD_DIV_CODE(chartType.name())
					.build();

			OverSeaDailyIndexChartPriceWrapper data = kisApi.getOverSeaInquireDailyChartPrice(headers, request);
			data.setOutput2(KisChartMapper.sortOverseaIndex(data.getOutput2()));

			chart = new OverSeaIndexChart();
			chart.setCode(id);
			chart.setData(data);
			chart.setChartType(chartType.name());
			chart = overSeaIndexChartDataService.save(chart);
		}

		return chart;
	}

	private KrIndexChart getKrIndexChart(String id, String code, ChartType chartType) throws Exception {
		KrIndexChart chart;
		try {
			chart = krIndexChartDataService.findByCodeAndChartTypeOrElseThrow(id, chartType.name());
		} catch (StockManagerException e) {
			HttpHeaders headers;
			headers = kisApiUtils.getDefaultApiHeader("FHKUP03500100");
			// 코스닥 지수
			KrDailyIndexChartPriceRequest request = KrDailyIndexChartPriceRequest.builder()
					.FID_COND_MRKT_DIV_CODE("U")
					.FID_INPUT_ISCD(code)
					.FID_INPUT_DATE_1(LocalDate.now().minusYears(100L).minusDays(1L).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
					.FID_INPUT_DATE_2(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
					.FID_PERIOD_DIV_CODE(chartType.name())
					.build();

			KrDailyIndexChartPriceWrapper data = kisApi.getKrInquireDailyIndexChartPrice(headers, request);
			data.setOutput2(KisChartMapper.sortKrIndex(data.getOutput2()));
			chart = new KrIndexChart();
			chart.setCode(id);
			chart.setData(data);
			chart.setChartType(chartType.name());
			chart = krIndexChartDataService.save(chart);
		}

		return chart;
	}

	public KrStockVolumeRank getKrStockVolumeList(String id) throws Exception {
		return krStockVolumeRankDataService.findById(id);
	}
}
