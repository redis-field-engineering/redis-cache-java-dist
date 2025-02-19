package com.redis.cache.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "")
public class DemoConfig {

	private Cache cache;
	private Search search;
	private Tmdb tmdb;

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	public Tmdb getTmdb() {
		return tmdb;
	}

	public void setTmdb(Tmdb tmdb) {
		this.tmdb = tmdb;
	}

	static class Tmdb {

		long sleep;
		String token;

		public long getSleep() {
			return sleep;
		}

		public void setSleep(long sleep) {
			this.sleep = sleep;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

	}

	static class Search {

		String index;

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

	}

	static class Cache {

		int preload;

		public int getPreload() {
			return preload;
		}

		public void setPreload(int preload) {
			this.preload = preload;
		}

	}
}
