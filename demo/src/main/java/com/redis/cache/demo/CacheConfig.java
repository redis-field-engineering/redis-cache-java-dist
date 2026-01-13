package com.redis.cache.demo;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redis.cache.RedisCacheConfiguration;
import com.redis.cache.RedisCacheManager;
import com.redis.cache.RedisType;
import com.redis.lettucemod.RedisModulesClient;

import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class CacheConfig {

	@Bean
	CacheManager cacheManager(RedisModulesClient client, MeterRegistry registry) {
		RedisCacheConfiguration hashConfig = RedisCacheConfiguration.defaultConfig().indexEnabled(true).meterRegistry(registry)
				.redisType(RedisType.HASH);
		RedisCacheConfiguration stringConfig = RedisCacheConfiguration.defaultConfig().meterRegistry(registry)
				.redisType(RedisType.STRING);
		return RedisCacheManager.builder(client) //
				.configuration("movie", hashConfig) //
				.configuration("popular", stringConfig) //
				.configuration("top-rated", stringConfig) //
				.configuration("upcoming", stringConfig) //
				.build();
	}

}