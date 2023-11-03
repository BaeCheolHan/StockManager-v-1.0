package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.api.kis.KisApi;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.dashboard.index.response.IndexChartResponse;
import com.my.stock.stockmanager.dto.kis.request.KrDailyIndexChartPriceRequest;
import com.my.stock.stockmanager.dto.kis.request.OverSeaDailyIndexChartPriceRequest;
import com.my.stock.stockmanager.dto.kis.response.KrDailyIndexChartPriceOutput2;
import com.my.stock.stockmanager.dto.kis.response.KrDailyIndexChartPriceWrapper;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyIndexChartPriceOutput2;
import com.my.stock.stockmanager.dto.kis.response.OverSeaDailyIndexChartPriceWrapper;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.redis.data.service.KrIndexChartDataService;
import com.my.stock.stockmanager.redis.data.service.OverSeaIndexChartDataService;
import com.my.stock.stockmanager.redis.entity.KrIndexChart;
import com.my.stock.stockmanager.redis.entity.OverSeaIndexChart;
import com.my.stock.stockmanager.utils.KisApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

  private final KisApi kisApi;
  private final KisApiUtils kisApiUtils;
  private final KrIndexChartDataService krIndexChartDataService;
  private final OverSeaIndexChartDataService overSeaIndexChartDataService;


  public IndexChartResponse getIndexChart(String chartType) throws Exception {
    KrIndexChart kospi = this.getKrIndexChart("kospi", "0001", chartType);
    KrIndexChart kosdaq = this.getKrIndexChart("kosdaq", "1001", chartType);

    OverSeaIndexChart snp = this.getOverSeaIndexChart("snp", "SPX", chartType);
    OverSeaIndexChart nasdaq = this.getOverSeaIndexChart("nasdaq", "COMP", chartType);
    return new IndexChartResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage(), kospi.getData(), kosdaq.getData(), snp.getData(), nasdaq.getData());
  }

  private OverSeaIndexChart getOverSeaIndexChart(String id, String code, String chartType) throws Exception {
    OverSeaIndexChart chart;
    try {
      chart = overSeaIndexChartDataService.findByCodeAndChartTypeOrElseThrow(id, chartType);
    } catch (StockManagerException e) {
      HttpHeaders headers;
      headers = kisApiUtils.getDefaultApiHeader("FHKST03030100");

      OverSeaDailyIndexChartPriceRequest request = OverSeaDailyIndexChartPriceRequest.builder()
          .FID_COND_MRKT_DIV_CODE("N")
          .FID_INPUT_DATE_1(LocalDate.now().minusYears(100L).minusDays(1L).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
          .FID_INPUT_DATE_2(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
          .FID_INPUT_ISCD(code)
          .FID_PERIOD_DIV_CODE(chartType)
          .build();

      OverSeaDailyIndexChartPriceWrapper data = kisApi.getOverSeaInquireDailyChartPrice(headers, request);
      final DateTimeFormatter dfm = DateTimeFormatter.ofPattern("yyyyMMdd");
      List<OverSeaDailyIndexChartPriceOutput2> output2 = data.getOutput2().stream().sorted(Comparator.comparing(s -> LocalDate.parse(s.getStck_bsop_date(), dfm))).toList();
      data.setOutput2(output2);

      chart = new OverSeaIndexChart();
      chart.setCode(id);
      chart.setData(data);
      chart.setChartType(chartType);
      chart = overSeaIndexChartDataService.save(chart);
    }

    return chart;
  }

  private KrIndexChart getKrIndexChart(String id, String code, String chartType) throws Exception {
    KrIndexChart chart;
    try {
      chart = krIndexChartDataService.findByCodeAndChartTypeOrElseThrow(id, chartType);
    } catch (StockManagerException e) {
      HttpHeaders headers;
      headers = kisApiUtils.getDefaultApiHeader("FHKUP03500100");
      // 코스닥 지수
      KrDailyIndexChartPriceRequest request = KrDailyIndexChartPriceRequest.builder()
          .FID_COND_MRKT_DIV_CODE("U")
          .FID_INPUT_ISCD(code)
          .FID_INPUT_DATE_1(LocalDate.now().minusYears(100L).minusDays(1L).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
          .FID_INPUT_DATE_2(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
          .FID_PERIOD_DIV_CODE(chartType)
          .build();

      KrDailyIndexChartPriceWrapper data = kisApi.getKrInquireDailyIndexChartPrice(headers, request);
      final DateTimeFormatter dfm = DateTimeFormatter.ofPattern("yyyyMMdd");
      List<KrDailyIndexChartPriceOutput2> output2 = data.getOutput2().stream().sorted(Comparator.comparing(s -> LocalDate.parse(s.getStck_bsop_date(), dfm))).toList();
      data.setOutput2(output2);
      chart = new KrIndexChart();
      chart.setCode(id);
      chart.setData(data);
      chart.setChartType(chartType);
      chart = krIndexChartDataService.save(chart);
    }

    return chart;
  }
}
