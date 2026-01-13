# Infrastructure

This directory contains Docker Compose configuration and supporting files for running the demo infrastructure.

## Services

- **Redis**: Redis Stack Server (includes Redis Search)
- **Prometheus**: Metrics collection
- **Grafana**: Metrics visualization with pre-configured dashboards

## Usage

Start all services:

```bash
docker compose up
```

Start in detached mode:

```bash
docker compose up -d
```

Stop all services:

```bash
docker compose down
```

## Accessing Services

- **Redis**: `localhost:6379`
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (username/password: `admin`/`admin`)

## Configuration

### Prometheus

Configuration: `prometheus/prometheus.yml`

Scrapes metrics from the demo application at `host.docker.internal:8080/actuator/prometheus`.

### Grafana

Provisioning files:
- `grafana/provisioning/datasources/datasources.yml` - Prometheus datasource
- `grafana/provisioning/dashboards/dashboards.yml` - Dashboard configuration
- `grafana/provisioning/dashboards/dashboard.json` - Redis Cache dashboard

The dashboard displays:
- Cache hit/miss rates
- Cache operation latencies
- HTTP request metrics
- Search operation metrics
