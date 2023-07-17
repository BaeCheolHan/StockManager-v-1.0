package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.base.response.BaseResponse;
import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.dividend.request.DividendRequest;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.service.DividendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class DividendController {

	private final DividendService dividendService;

	@PostMapping("/dividend")
	public BaseResponse saveDividend(@RequestBody DividendRequest request) throws StockManagerException {
		dividendService.saveDividend(request);
		return new BaseResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage());
	}
}
