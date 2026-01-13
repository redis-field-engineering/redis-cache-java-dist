# Redis Cache Java Demo

This is a Spring Boot demo application showcasing Redis Cache Java features.

## Prerequisites

- Java 17 or later
- Docker and Docker Compose
- TMDB API token (get one at https://developer.themoviedb.org/docs/getting-started)

## Quick Start

### 1. Start Infrastructure

From the repository root, start Redis, Prometheus, and Grafana:

```bash
cd ../infrastructure
docker compose up
```

### 2. Set TMDB Token

```bash
export TMDB_TOKEN=<your API read-access token>
```

### 3. Run the Demo

```bash
./gradlew bootRun
```

### 4. Access the Application

- **Demo App**: http://localhost:8080
- **Grafana Dashboard**: http://localhost:3000/d/1 (username/password: `admin`/`admin`)
- **Prometheus**: http://localhost:9090

## What to Try

1. Click through different movie pages and notice response times improve after first request
2. View the Grafana dashboard to see cache hit/miss metrics
3. Try the Search feature to see Redis-powered search in action

## Building

```bash
./gradlew build
```

## Running Tests

```bash
./gradlew test
```

## Configuration

The demo uses the following Redis Cache features:

- **Cache Manager**: `RedisCacheManager` with Spring Cache abstraction
- **Metrics**: Micrometer metrics exported to Prometheus
- **Indexing**: Redis Search for movie search functionality
- **TTL**: Configurable time-to-live for cache entries

See `src/main/java/com/redis/cache/demo/CacheConfig.java` for configuration details.

## Documentation

For complete documentation on Redis Cache Java, visit:
https://redis-field-engineering.github.io/redis-cache-java
