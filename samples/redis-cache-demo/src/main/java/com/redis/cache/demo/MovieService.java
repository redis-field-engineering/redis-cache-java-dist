package com.redis.cache.demo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import info.movito.themoviedbapi.model.movies.MovieDb;

@Service
public class MovieService {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat TARGET_DATE_FORMAT = new SimpleDateFormat("MMM d, yyyy");

	private static final Logger log = LoggerFactory.getLogger(MovieService.class);

	@Autowired
	TmdbService tmdbService;

	public MovieDb getMovie(Integer id) throws Exception {
		return tmdbService.fetchMovie(id);
	}

	@Cacheable("popular")
	public List<MovieDb> fetchPopularMovies(int page) throws Exception {
		return movies(tmdbService.fetchPopularMovies(page));
	}

	@Cacheable("top-rated")
	public List<MovieDb> fetchTopRatedMovies(int page) throws Exception {
		return movies(tmdbService.fetchTopRatedMovies(page));
	}

	@Cacheable("upcoming")
	public List<MovieDb> fetchUpcomingMovies(int page) throws Exception {
		return movies(tmdbService.fetchUpcomingMovies(page));
	}

	private List<MovieDb> movies(List<Integer> ids) {
		return ids.stream().map(this::fetchMovie).collect(Collectors.toList());
	}

	public MovieDb fetchMovie(Integer id) {
		MovieDb movie;
		try {
			movie = tmdbService.fetchMovie(id);
		} catch (Exception e) {
			throw new RuntimeException("Could not fetch movie " + id, e);
		}
		if (StringUtils.hasLength(movie.getReleaseDate())) {
			try {
				Date date = DATE_FORMAT.parse(movie.getReleaseDate());
				movie.setReleaseDate(TARGET_DATE_FORMAT.format(date));
			} catch (ParseException e) {
				log.info("Could not parse {}", movie.getReleaseDate());
			}
		}
		return movie;

	}
}
