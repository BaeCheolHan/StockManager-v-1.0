package com.my.stock.stockmanager.dto.asset;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AssetChart {
	private List<String> xaxisCategories;
	private List<BigDecimal> investmentAmountList;
	private List<BigDecimal> evaluationAmountList;
}
