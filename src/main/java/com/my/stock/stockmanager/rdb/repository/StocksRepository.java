package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.Stock;
import com.my.stock.stockmanager.rdb.entity.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StocksRepository extends JpaRepository<Stocks, String> {
	List<Stocks> findAllByNational(String national);

	List<Stocks> findAllByNationalNot(String national);
}
