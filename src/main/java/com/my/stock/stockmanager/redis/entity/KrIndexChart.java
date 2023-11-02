package com.my.stock.stockmanager.redis.entity;

import com.my.stock.stockmanager.dto.kis.response.KrDailyIndexChartPriceWrapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;


@Getter
@Setter
@RedisHash("KrIndexChart")
public class KrIndexChart {
	@Id
	private String code;

	private KrDailyIndexChartPriceWrapper data;

	@TimeToLive(unit = TimeUnit.MINUTES)
	@Builder.Default
	private Long expiration = 3L;
}
