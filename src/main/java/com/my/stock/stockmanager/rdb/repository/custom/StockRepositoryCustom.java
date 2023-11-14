package com.my.stock.stockmanager.rdb.repository.custom;

import com.my.stock.stockmanager.dto.stock.DashboardStock;

import java.util.List;

public interface StockRepositoryCustom {
	List<DashboardStock> findAllDashboardStock(String memberId, Long bankId);
}
