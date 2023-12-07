package com.my.stock.stockmanager.rdb.repository.custom;

import com.my.stock.stockmanager.dto.dividend.DividendInfoByItem;
import com.my.stock.stockmanager.dto.dividend.DividendSumByMonth;
import com.my.stock.stockmanager.dto.dividend.DividendInfoDto;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

public interface DividendRepositoryCustom {

	List<DividendSumByMonth> findDividendChartByMemberId(String memberId);
	List<DividendInfoDto> findAllByMemberIdOrderByYearMonthDayAsc(String memberId, Sort sort);

	List<DividendInfoByItem> findDividendInfoByMemberIdGroupBySymbol(String memberId, BigDecimal basePrice);
}
