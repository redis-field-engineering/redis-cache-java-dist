package com.redis.cache.demo;

import static com.redis.cache.demo.SearchService.FIELD_ID;
import static com.redis.cache.demo.SearchService.FIELD_POPULARITY;
import static com.redis.cache.demo.SearchService.FIELD_RELEASE_DATE;
import static com.redis.cache.demo.SearchService.FIELD_RUNTIME;
import static com.redis.cache.demo.SearchService.FIELD_STATUS;
import static com.redis.cache.demo.SearchService.FIELD_TAGLINE;
import static com.redis.cache.demo.SearchService.FIELD_VOTE_AVERAGE;
import static com.redis.cache.demo.SearchService.FIELD_VOTE_COUNT;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.redis.cache.RedisCache;
import com.redis.cache.RedisCacheManager;

@Controller
public class MovieController {

    @Autowired
    MovieService movieService;

    @Autowired
    SearchService searchService;

    @Autowired
    RedisCacheManager cacheManager;

    @GetMapping("/movie/{id}")
    public String movie(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("movie", movieService.fetchMovie(id));
        return "movie";
    }

    @GetMapping({ "/movies/popular", "/" })
    public String getPopularMovies(Model model, @RequestParam(defaultValue = "1", name = "page") int page) throws Exception {
        // Add the movies to the model
        model.addAttribute("movies", movieService.fetchPopularMovies(page));
        model.addAttribute("page", page);
        // Return the view name
        return "popular";
    }

    @GetMapping("/movies/top-rated")
    public String getTopRatedMovies(Model model, @RequestParam(defaultValue = "1", name = "page") int page) throws Exception {

        // Add the movies to the model
        model.addAttribute("movies", movieService.fetchTopRatedMovies(page));
        model.addAttribute("page", page);

        // Return the view name
        return "top-rated";
    }

    @GetMapping("/movies/upcoming")
    public String getUpcomingMovies(Model model, @RequestParam(defaultValue = "1", name = "page") int page) throws Exception {
        // Add the movies to the model
        model.addAttribute("movies", movieService.fetchUpcomingMovies(page));
        model.addAttribute("page", page);

        // Return the view name
        return "upcoming";
    }

    @GetMapping("/movies/search")
    public String search(Model model, @RequestParam(name = "query", defaultValue = "") String query) {
        // Add the movies to the model
        model.addAttribute("movies", searchService.searchMovies(query));
        model.addAttribute("query", query);
        model.addAttribute("fields", Arrays.asList(FIELD_ID, FIELD_POPULARITY, FIELD_RELEASE_DATE, FIELD_RUNTIME, FIELD_STATUS,
                FIELD_TAGLINE, FIELD_VOTE_AVERAGE, FIELD_VOTE_COUNT));

        // Return the view name
        return "search";
    }

    @GetMapping("/cache")
    public String cacheInfo(Model model) {
        Map<String, Long> cacheInfo = new HashMap<>();
        RedisCache cache = (RedisCache) cacheManager.getCache("movie");
        cacheInfo.put("movie", cache.getCount());
        model.addAttribute("cacheInfo", cacheInfo);
        return "cache";
    }

}
