package com.my.stock.stockmanager.rdb.repository;

import com.my.stock.stockmanager.rdb.entity.Dividend;
import com.my.stock.stockmanager.rdb.repository.custom.DividendRepositoryCustom;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<Dividend, Long>, DividendRepositoryCustom {
	@Query(value = "SELECT year FROM dividend WHERE member_id = :memberId GROUP BY year ORDER BY year ASC", nativeQuery = true)
	List<Integer> findYearByMemberIdGroupByYear(Long memberId);

	@Query(value="SELECT sum(dividend) AS totalDividend FROM stock_manager.dividend WHERE member_id = :memberId AND symbol = :symbol", nativeQuery = true )
	BigDecimal findDividendSumByMemberIdAndSymbol(Long memberId, String symbol);
}
