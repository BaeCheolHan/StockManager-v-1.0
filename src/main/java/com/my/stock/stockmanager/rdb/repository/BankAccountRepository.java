package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

	@Query("SELECT b FROM BankAccount b JOIN FETCH b.stocks WHERE b.id = :id")
	Optional<BankAccount> findByIdJoinFetch(Long id);
}
