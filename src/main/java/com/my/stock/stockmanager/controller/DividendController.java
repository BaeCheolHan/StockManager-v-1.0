package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.dividend.request.DividendRequest;
import com.my.stock.stockmanager.dto.dividend.response.DividendChartResponse;
import com.my.stock.stockmanager.dto.dividend.response.DividendInfoByItemResponse;
import com.my.stock.stockmanager.dto.dividend.response.DividendInfoResponse;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.service.DividendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/api/dividend")
@RestController
@RequiredArgsConstructor
public class DividendController {

	private final DividendService dividendService;

	@PostMapping
	public BaseResponse saveDividend(@RequestBody DividendRequest request) throws StockManagerException {
		dividendService.saveDividend(request);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@GetMapping("/{memberId}/chart")
	public DividendChartResponse getDividendChartByMonth(@PathVariable String memberId) {
		return new DividendChartResponse(dividendService.getDividendChart(memberId), ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@GetMapping("/member/{memberId}")
	public DividendInfoResponse getDividends(@PathVariable String memberId) {
		return new DividendInfoResponse(dividendService.getDividends(memberId), ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}

	@GetMapping("/by-item/{memberId}")
	public DividendInfoByItemResponse getAllDividendsByItm(@PathVariable String memberId) throws IOException {
		return new DividendInfoByItemResponse(dividendService.getAllDividendsByItm(memberId), ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}
}
