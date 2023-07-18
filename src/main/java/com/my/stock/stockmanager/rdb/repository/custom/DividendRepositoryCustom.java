package com.my.stock.stockmanager.rdb.repository.custom;

import com.my.stock.stockmanager.dto.dividend.DividendSumByMonth;

import java.util.List;

public interface DividendRepositoryCustom {

	List<DividendSumByMonth> findDividendChartByMemberId(Long memberId);
}
