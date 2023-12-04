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


	@Bean
	public RedisConfiguration redisStandaloneConfiguration() {
			if ("Y".equals(cMode)) {
				return new RedisClusterConfiguration(Arrays.asList(redisHost.split(",")));
			} else {
				String[] temp = redisHost.split(":");
				return new RedisStandaloneConfiguration(temp[0], Integer.parseInt(temp[1]));
			}
	}

	@Bean
	public RedisConnectionFactory lettuceConnectionFactory() {
			return new LettuceConnectionFactory(redisStandaloneConfiguration());
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