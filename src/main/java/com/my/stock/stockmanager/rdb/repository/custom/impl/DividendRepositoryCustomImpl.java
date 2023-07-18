package com.my.stock.stockmanager.rdb.repository.custom.impl;

import com.my.stock.stockmanager.dto.dividend.DividendSumByMonth;
import com.my.stock.stockmanager.rdb.entity.QDividend;
import com.my.stock.stockmanager.rdb.entity.QExchangeRate;
import com.my.stock.stockmanager.rdb.entity.QStocks;
import com.my.stock.stockmanager.rdb.repository.custom.DividendRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DividendRepositoryCustomImpl implements DividendRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<DividendSumByMonth> findDividendChartByMemberId(Long memberId) {
		QDividend dividend = QDividend.dividend1;
		QStocks stocks = QStocks.stocks;
		QExchangeRate exchangeRate = QExchangeRate.exchangeRate;

		return queryFactory.from(dividend)
				.select(Projections.fields(DividendSumByMonth.class, dividend.year, dividend.month, stocks.national
						.when("KR").then(dividend.dividend)
						.otherwise(dividend.dividend.multiply(exchangeRate.basePrice)).sum().as("dividend")))
				.innerJoin(stocks).on(dividend.symbol.eq(stocks.symbol))
				.join(exchangeRate)
				.where(dividend.memberId.eq(memberId))
				.groupBy(dividend.year, dividend.month)
				.orderBy(dividend.year.asc(), dividend.month.asc())
				.fetch();
	}
}
