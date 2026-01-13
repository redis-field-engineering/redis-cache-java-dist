package com.redis.cache.demo;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

	private static final Logger log = LoggerFactory.getLogger(CacheService.class);

	@Autowired
	CacheManager cacheManager;

	@Autowired
	MovieService movieService;

	@Autowired
	DemoConfig demoConfig;

	public void loadMovieCache() throws Exception {
		cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
		for (int page = 1; page <= demoConfig.getCache().getPreload(); page++) {
			log.info("Loading popular movies page {}", page);
			movieService.fetchPopularMovies(page);
			log.info("Loading top-rated movies page {}", page);
			movieService.fetchTopRatedMovies(page);
			log.info("Loading upcoming movies page {}", page);
			movieService.fetchUpcomingMovies(page);
		}
	}

	@Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
	public void refreshMovieCache() throws Exception {
		loadMovieCache();
	}

	@EventListener
	public void onApplicationEvent(ApplicationStartedEvent event) throws Exception {
		loadMovieCache();
	}

}
