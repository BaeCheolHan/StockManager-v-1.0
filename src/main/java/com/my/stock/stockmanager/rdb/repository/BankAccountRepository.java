package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
