package com.my.stock.stockmanager.rdb.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_search_stat")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockSearchStat {

	@Id
	@Column(name = "symbol", length = 40)
	private String symbol;

	@Column(name = "hit_count", nullable = false)
	private long hitCount;

	@Column(name = "last_hit_at", nullable = false)
	private LocalDateTime lastHitAt;

	public void increase() {
		this.hitCount += 1;
		this.lastHitAt = LocalDateTime.now();
	}
}
