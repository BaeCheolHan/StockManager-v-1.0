package com.my.stock.stockmanager.controller;

import com.my.stock.stockmanager.constants.ResponseCode;
import com.my.stock.stockmanager.dto.asset.AssetChart;
import com.my.stock.stockmanager.dto.asset.Response.AssetChartResponse;
import com.my.stock.stockmanager.dto.bank.account.response.BankAccountResponse;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/asset")
public class AssetController {

	private final AssetService assetService;

	@GetMapping("/member/{memberId}/chart")
	public AssetChartResponse getAssetChart(@PathVariable Long memberId) throws StockManagerException {
		AssetChart result = assetService.getAssetChartData(memberId);
		return new AssetChartResponse(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage(), result);
	}

}
