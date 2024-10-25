package com.redis.cache.demo;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.redis.cache.demo.DemoConfig.Search;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.search.CreateOptions;
import com.redis.lettucemod.search.CreateOptions.DataType;
import com.redis.lettucemod.search.Field;
import com.redis.lettucemod.search.SearchResults;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Service
public class SearchService {

	public static final String FIELD_RUNTIME = "runtime";
	public static final String FIELD_OVERVIEW = "overview";
	public static final String FIELD_POPULARITY = "popularity";
	public static final String FIELD_STATUS = "status";
	public static final String FIELD_TAGLINE = "tagline";
	public static final String FIELD_VOTE_COUNT = "voteCount";
	public static final String FIELD_VOTE_AVERAGE = "voteAverage";
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_RELEASE_DATE = "releaseDate";
	public static final String FIELD_ID = "id";

	private final Search config;
	private final StatefulRedisModulesConnection<String, String> connection;
	private final Timer timer;

	public SearchService(StatefulRedisModulesConnection<String, String> connection, DemoConfig config,
			MeterRegistry registry) {
		this.connection = connection;
		this.config = config.getSearch();
		this.timer = registry.timer("redis.requests", "command", "search");
	}

	@EventListener
	public void onApplicationEvent(ApplicationStartedEvent event) {
		try {
			createIndex();
		} catch (Exception e) {
			// Index already exists - ignore.
		}
	}

	@SuppressWarnings("unchecked")
	private void createIndex() {
		CreateOptions<String, String> options = CreateOptions.<String, String>builder().on(DataType.HASH)
				.prefix("movie:").build();
		connection.sync().ftCreate(config.getIndex(), options, Field.numeric(FIELD_RUNTIME).build(),
				Field.text(FIELD_OVERVIEW).build(), Field.numeric(FIELD_POPULARITY).build(),
				Field.tag(FIELD_STATUS).build(), Field.text(FIELD_TAGLINE).build(), Field.text(FIELD_TITLE).build(),
				Field.numeric(FIELD_VOTE_AVERAGE).build(), Field.numeric(FIELD_VOTE_COUNT).build(),
				Field.tag(FIELD_RELEASE_DATE).build(), Field.tag(FIELD_ID).build());
	}

	public SearchResults<String, String> searchMovies(String query) {
		return timer.record(() -> connection.sync().ftSearch(config.getIndex(), query));
	}

}
