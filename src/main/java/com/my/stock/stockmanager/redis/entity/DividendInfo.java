package com.my.stock.stockmanager.redis.entity;

import com.my.stock.stockmanager.dto.dividend.StockDividendHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@RedisHash("DividendInfo")
public class DividendInfo {
	@Id
	private String symbol;

	private BigDecimal annualDividend = BigDecimal.ZERO;
	private BigDecimal dividendRate = BigDecimal.ZERO;
	private List<StockDividendHistory> dividendHistories = new ArrayList<>();

}
