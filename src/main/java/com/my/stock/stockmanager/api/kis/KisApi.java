package com.my.stock.stockmanager.api.kis;

import com.my.stock.stockmanager.dto.kis.request.*;
import com.my.stock.stockmanager.dto.kis.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KisApi", url = "https://openapi.koreainvestment.com:9443/")
public interface KisApi {

	// 해외 개별 주식 상세
	@GetMapping("uapi/overseas-price/v1/quotations/price-detail")
	OverSeaNowStockPriceWrapper getOverSeaStockPrice(@RequestHeader HttpHeaders header, @SpringQueryMap OverSeaStockPriceRequest param);
	// 국내 개별 주식 상세
	@GetMapping("uapi/domestic-stock/v1/quotations/inquire-price")
	KrNowStockPriceWrapper getKorStockPrice(@RequestHeader HttpHeaders header, @SpringQueryMap KrStockPriceRequest param);
	// 국내 기간별 시세 차트
	@GetMapping("uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice")
	KrDailyStockChartPriceWrapper getKrDailyStockChartPrice(@RequestHeader HttpHeaders header, @SpringQueryMap KrDailyStockChartPriceRequest param);
	// 해외 기간별 시세 차트
	@GetMapping("uapi/overseas-price/v1/quotations/dailyprice")
	OverSeaDailyStockChartPriceWrapper getOverSeaDailyStockChartPrice(@RequestHeader HttpHeaders header, @SpringQueryMap OverSeaDailyStockChartPriceRequest param);

	// 국내주식 지수 api
	@GetMapping("uapi/domestic-stock/v1/quotations/inquire-daily-indexchartprice")
	KrDailyIndexChartPriceWrapper getKrInquireDailyIndexChartPrice(@RequestHeader HttpHeaders header, @SpringQueryMap KrDailyIndexChartPriceRequest param);

	// 해외주식 지수 api
	@GetMapping("uapi/overseas-price/v1/quotations/inquire-daily-chartprice")
	OverSeaDailyIndexChartPriceWrapper getOverSeaInquireDailyChartPrice(@RequestHeader HttpHeaders header, @SpringQueryMap OverSeaDailyIndexChartPriceRequest param);

	@GetMapping("uapi/domestic-stock/v1/quotations/volume-rank")
	KrStockVolumeRankWrapper getStockVolumeRank(@RequestHeader HttpHeaders header, @SpringQueryMap KrStockVolumeRankRequest param);
}
