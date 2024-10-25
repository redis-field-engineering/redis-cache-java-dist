package com.redis.cache.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RedisCacheDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisCacheDemoApplication.class, args);
	}

}
