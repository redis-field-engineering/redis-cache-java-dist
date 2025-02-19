package com.redis.cache.demo;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.redis.cache.demo.DemoConfig.Tmdb;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.core.Movie;
import info.movito.themoviedbapi.model.movies.MovieDb;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Service
public class TmdbService {

	private static final Logger log = LoggerFactory.getLogger(TmdbService.class);

	private static final String METER_TIMER = "tmdb.api.requests";
	private final Locale locale = Locale.getDefault();
	private final String language = locale.getLanguage();
	private final String country = locale.getCountry();

	private final Tmdb config;
	private final TmdbApi tmdbApi;
	private final Timer popularTimer;
	private final Timer topRatedTimer;
	private final Timer upcomingTimer;
	private final Timer movieTimer;

	public TmdbService(DemoConfig config, MeterRegistry registry) {
		this.config = config.getTmdb();
		log.info("Using TMDB API token: {}", this.config.getToken());
		this.tmdbApi = new TmdbApi(this.config.getToken());
		this.popularTimer = registry.timer(METER_TIMER, "method", "popular");
		this.topRatedTimer = registry.timer(METER_TIMER, "method", "top-rated");
		this.upcomingTimer = registry.timer(METER_TIMER, "method", "upcoming");
		this.movieTimer = registry.timer(METER_TIMER, "method", "movie");
	}

	@Cacheable("movie")
	public MovieDb fetchMovie(Integer id) throws Exception {
		return movieTimer.recordCallable(() -> {
			if (config.getSleep() > 0) {
				Thread.sleep(config.getSleep());
			}
			return tmdbApi.getMovies().getDetails(id, language);
		});
	}

	public List<Integer> fetchPopularMovies(int page) throws Exception {
		return popularTimer
				.recordCallable(() -> ids(tmdbApi.getMovieLists().getPopular(language, page, country).getResults()));
	}

	private List<Integer> ids(List<Movie> movies) {
		return movies.stream().filter(this::filterMovie).map(Movie::getId).collect(Collectors.toList());
	}

	public List<Integer> fetchTopRatedMovies(int page) throws Exception {
		return topRatedTimer
				.recordCallable(() -> ids(tmdbApi.getMovieLists().getTopRated(language, page, country).getResults()));
	}

	public List<Integer> fetchUpcomingMovies(int page) throws Exception {
		return upcomingTimer
				.recordCallable(() -> ids(tmdbApi.getMovieLists().getUpcoming(language, 1, country).getResults()));
	}

	private boolean filterMovie(Movie movie) {
		return !movie.getAdult();
	}

}
