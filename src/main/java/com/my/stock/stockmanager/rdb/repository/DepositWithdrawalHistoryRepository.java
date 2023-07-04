package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.DepositWithdrawalHistory;
import com.my.stock.stockmanager.rdb.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositWithdrawalHistoryRepository extends JpaRepository<DepositWithdrawalHistory, Long> {
}
