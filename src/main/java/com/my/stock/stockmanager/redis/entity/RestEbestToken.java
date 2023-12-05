package com.my.stock.stockmanager.redis.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Setter
@RedisHash("RestEbestToken")
public class RestEbestToken {
	@Id
	private String access_token;
	private String token_type;
	private String scope;

	@TimeToLive
	private int expires_in;
}
