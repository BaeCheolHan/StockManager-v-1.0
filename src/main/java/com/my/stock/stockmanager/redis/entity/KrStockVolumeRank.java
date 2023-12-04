package com.my.stock.stockmanager.redis.entity;

import com.my.stock.stockmanager.dto.kis.response.KrStockVolumeRankWrapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@RedisHash("KrStockVolumeRank")
public class KrStockVolumeRank {
	@Id
	private String id;

	private KrStockVolumeRankWrapper data;

	@TimeToLive(unit = TimeUnit.MINUTES)
	@Builder.Default
	private Long expiration = 3L;
}
