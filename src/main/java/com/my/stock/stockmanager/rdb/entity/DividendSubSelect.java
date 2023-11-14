package com.my.stock.stockmanager.rdb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import java.math.BigDecimal;

@Entity
@Subselect("SELECT @ROWNUM:=@ROWNUM+1 AS id, member_id, symbol, SUM(dividend) AS total_dividend FROM dividend , (SELECT @ROWNUM:=0) AS R GROUP BY symbol, member_id")
@Immutable
@Synchronize("dividend")
public class DividendSubSelect {

	@Id
	@GeneratedValue
	private Long id;

	private String memberId;

	private String symbol;

	@Column(name = "total_dividend")
	private BigDecimal totalDividend;
}
