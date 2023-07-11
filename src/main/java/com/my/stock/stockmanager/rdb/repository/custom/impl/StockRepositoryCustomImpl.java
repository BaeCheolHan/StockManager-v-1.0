package com.my.stock.stockmanager.rdb.repository.custom.impl;

import com.my.stock.stockmanager.dto.stock.DashboardStock;
import com.my.stock.stockmanager.rdb.entity.QBankAccount;
import com.my.stock.stockmanager.rdb.entity.QMember;
import com.my.stock.stockmanager.rdb.entity.QStock;
import com.my.stock.stockmanager.rdb.entity.QStocks;
import com.my.stock.stockmanager.rdb.repository.custom.StockRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockRepositoryCustomImpl implements StockRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<DashboardStock> findAllDashboardStock(Long memberId, Long bankId) {
		QStock stock = QStock.stock;
		QStocks stocks = QStocks.stocks;
		QBankAccount bankAccount = QBankAccount.bankAccount;
		BooleanBuilder builder = new BooleanBuilder();
		if(memberId != null) {
			builder.and(bankAccount.member.id.eq(memberId));
		}

		if(bankId != null) {
			builder.and(bankAccount.id.eq(bankId));
		}

		return queryFactory.from(stock)
				.select(Projections.fields(DashboardStock.class,
						stock.symbol,
						stocks.code,
						stocks.national,
						stocks.name,
						stock.price.multiply(stock.quantity).sum().divide(stock.quantity.sum()).as("avgPrice"),
						stock.quantity.sum().as("quantity")
						))
				.innerJoin(stocks).on(stock.symbol.eq(stocks.symbol))
				.innerJoin(bankAccount).on(stock.bankAccount.id.eq(bankAccount.id))
				.where(builder)
				.orderBy(stock.quantity.asc())
				.groupBy(stock.symbol)
				.fetch();
	}
}
