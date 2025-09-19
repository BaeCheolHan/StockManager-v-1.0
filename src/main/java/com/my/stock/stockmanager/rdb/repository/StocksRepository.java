package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StocksRepository extends JpaRepository<Stocks, String> {

	@Query(value = "SELECT code FROM stocks WHERE national = :national GROUP BY code", nativeQuery = true)
	List<String> findCodeByNationalGroupByCode(@Param("national")String national);

	List<Stocks> findAllByCode(String code);

	Optional<Stocks> findBySymbol(String symbol);

	List<Stocks> findAllBySymbolLike(String symbol);

	@Query(value = "SELECT s.* FROM stocks s LEFT JOIN stock_search_stat t ON t.symbol = s.symbol WHERE s.name LIKE %:q% OR s.symbol LIKE %:q% ORDER BY (CASE WHEN s.name LIKE :qprefix THEN 3 WHEN s.name LIKE %:q% THEN 2 WHEN s.symbol LIKE %:q% THEN 1 ELSE 0 END) DESC, COALESCE(t.hit_count,0) DESC, s.name LIMIT :limit", nativeQuery = true)
	List<Stocks> searchByKeyword(@Param("q") String q, @Param("qprefix") String qprefix, @Param("limit") int limit);

	@Query(value = "SELECT s.* FROM stocks s JOIN stock_search_stat t ON t.symbol = s.symbol WHERE t.last_hit_at > NOW() - INTERVAL 30 DAY ORDER BY t.hit_count DESC, t.last_hit_at DESC LIMIT :limit", nativeQuery = true)
	List<Stocks> findTrending(@Param("limit") int limit);
}
