package com.my.stock.stockmanager.rdb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stocks {
	// 종목 코드
	@Id
	private String symbol;

	// KOSPI, KOSDAQ...
	private String code;

	// 종목명
	@NotNull
	private String name;

	// 국가코드
	@NotNull
	private String national;

	// 통화 구분
	@NotNull
	private String currency;
}
