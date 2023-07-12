package com.my.stock.stockmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.util.Arrays;

@Configuration
@EnableRedisHttpSession
public class RedisConfig {
	@Value("${spring.data.redis.host}")
	private String redisHost;

	@Value("${spring.data.redis.cMode}")
	private String cMode;

	private RedisConfiguration redisConfiguration;
	private LettuceConnectionFactory redisConnectionFactory;

	@Bean
	public RedisConfiguration redisStandaloneConfiguration() {
		if (redisConfiguration == null) {
			if ("Y".equals(cMode)) {
				redisConfiguration = new RedisClusterConfiguration(Arrays.asList(redisHost.split(",")));
			} else {
				String[] temp = redisHost.split(":");
				redisConfiguration = new RedisStandaloneConfiguration(temp[0], Integer.parseInt(temp[1]));
			}
		}
		return redisConfiguration;
	}

	@Bean
	public RedisConnectionFactory lettuceConnectionFactory() {
		if (redisConnectionFactory == null) {
			redisConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration());
		}
		return redisConnectionFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(lettuceConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}
}