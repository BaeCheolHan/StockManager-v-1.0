package com.my.stock.stockmanager.redis.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Setter
@RedisHash("RestKisToken")
public class RestKisToken {
	@Id
	private String access_token;
	private String access_token_token_expired;
	private String token_type;

	@TimeToLive
	private int expires_in;
}
