package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.constants.ChartType;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.dashboard.index.response.IndexChartResponse;
import com.my.stock.stockmanager.dto.dashboard.volumerank.response.KrStockVolumeRankResponse;
import com.my.stock.stockmanager.redis.entity.KrStockVolumeRank;
import com.my.stock.stockmanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

	private final DashboardService dashboardService;

	@GetMapping("/index-chart/{chartType}")
	public IndexChartResponse getIndexChart(@PathVariable ChartType chartType) throws Exception {
		return dashboardService.getIndexChart(chartType.name());
	}

	@GetMapping("/kr/volume/{id}")
	public KrStockVolumeRankResponse getKrStockVolumeList(@PathVariable String id) throws Exception {
		KrStockVolumeRank response = dashboardService.getKrStockVolumeList(id);
		return new KrStockVolumeRankResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage(), response.getData().getOutput());
	}
}
