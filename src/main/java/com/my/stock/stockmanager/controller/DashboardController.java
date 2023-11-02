package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.dashboard.index.response.IndexChartResponse;
import com.my.stock.stockmanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

	private final DashboardService dashboardService;

	@GetMapping("/index-chart")
	public IndexChartResponse getIndexChart() throws Exception {
		IndexChartResponse response = dashboardService.getIndexChart();
		return response;
	}
}
