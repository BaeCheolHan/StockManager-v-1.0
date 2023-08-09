package com.my.stock.stockmanager.service;

import com.my.stock.stockmanager.dto.asset.AssetChart;
import com.my.stock.stockmanager.exception.StockManagerException;
import com.my.stock.stockmanager.rdb.data.service.MemberDataService;
import com.my.stock.stockmanager.rdb.entity.DailyTotalInvestmentAmount;
import com.my.stock.stockmanager.rdb.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {
	private final MemberDataService memberDataService;

	@Transactional
	public AssetChart getAssetChartData(Long memberId) throws StockManagerException {
		Member member = memberDataService.findById(memberId);
		List<DailyTotalInvestmentAmount> dailyTotalInvestmentAmounts = member.getDailyTotalInvestmentAmounts();
		AssetChart dto = new AssetChart();
		dto.setXaxisCategories(dailyTotalInvestmentAmounts.stream().map(entity -> entity.getDate().toString()).toList());
		dto.setInvestmentAmountList(dailyTotalInvestmentAmounts.stream().map(DailyTotalInvestmentAmount::getTotalInvestmentAmount).toList());
		dto.setEvaluationAmountList(dailyTotalInvestmentAmounts.stream().map(DailyTotalInvestmentAmount::getEvaluationAmount).toList());
		return dto;
	}


}
