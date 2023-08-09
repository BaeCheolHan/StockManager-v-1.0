package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.DailyTotalInvestmentAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyTotalInvestmentAmountRepository extends JpaRepository<DailyTotalInvestmentAmount, Long> {
}
