package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.StockSearchStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockSearchStatRepository extends JpaRepository<StockSearchStat, String> {
}
