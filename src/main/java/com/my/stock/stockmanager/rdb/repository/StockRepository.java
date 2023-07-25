package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.Stock;
import com.my.stock.stockmanager.rdb.repository.custom.StockRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>, StockRepositoryCustom {
	int countBySymbol(String symbol);

	Optional<Stock> findFirstBySymbol(String symbol);

	void deleteAllByBankAccountId(Long id);
}
