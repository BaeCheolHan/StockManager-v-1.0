package com.my.stock.stockmanager.rdb.repository.custom;

import com.my.stock.stockmanager.dto.dividend.DividendSumByMonth;
import com.my.stock.stockmanager.dto.dividend.DividendInfo;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface DividendRepositoryCustom {

	List<DividendSumByMonth> findDividendChartByMemberId(Long memberId);
	List<DividendInfo> findAllByMemberIdOrderByYearMonthDayAsc(Long memberId, Sort sort);
}
