package com.my.stock.stockmanager.rdb.repository.custom.impl;

import com.my.stock.stockmanager.dto.stock.DashboardStock;
import com.my.stock.stockmanager.rdb.entity.*;
import com.my.stock.stockmanager.rdb.repository.custom.StockRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockRepositoryCustomImpl implements StockRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<DashboardStock> findAllDashboardStock(String memberId, Long bankId) {
		QStock stock = QStock.stock;
		QStocks stocks = QStocks.stocks;
		QBankAccount bankAccount = QBankAccount.bankAccount;
		QDividendSubSelect dsub = QDividendSubSelect.dividendSubSelect;

		BooleanBuilder where = new BooleanBuilder();
		if (memberId != null) {
			where.and(bankAccount.member.id.eq(memberId));
		}
		if (bankId != null) {
			where.and(bankAccount.id.eq(bankId));
		}

		// sum(price*qty) / NULLIF(sum(qty),0)
		NumberExpression<BigDecimal> sumPxQ =
				stock.price.multiply(stock.quantity).sum();
		NumberExpression<BigDecimal> sumQ =
				stock.quantity.sum().castToNum(BigDecimal.class);

		NumberExpression<BigDecimal> avgPriceSafe =
				Expressions.numberTemplate(
						BigDecimal.class,
						"({0}) / NULLIF({1},0)",
						sumPxQ, sumQ
				);

		return queryFactory
				.from(stock)
				.innerJoin(stocks).on(stock.symbol.eq(stocks.symbol))
				.innerJoin(bankAccount).on(stock.bankAccount.id.eq(bankAccount.id))
				// DividendSubSelect는 memberId + symbol 복합키(@IdClass)로 고쳤다고 가정
				.leftJoin(dsub).on(
						dsub.symbol.eq(stock.symbol)
								.and(dsub.memberId.eq(bankAccount.member.id))
				)
				.where(where)
				.select(Projections.fields(
						DashboardStock.class,
						stock.symbol.as("symbol"),
						stocks.code.as("code"),
						stocks.national.as("national"),
						stocks.name.as("name"),
						avgPriceSafe.as("avgPrice"),
						sumQ.as("quantity"),
						dsub.totalDividend.coalesce(BigDecimal.ZERO).as("totalDividend")
				))
				// SELECT에 있는 비집계 컬럼은 전부 groupBy
				.groupBy(
						stock.symbol,
						stocks.code,
						stocks.national,
						stocks.name
				)
				// 집계 기준으로 정렬 (원하면 바꿔도 됨)
				.orderBy(sumQ.desc())
				.fetch();
	}
}
