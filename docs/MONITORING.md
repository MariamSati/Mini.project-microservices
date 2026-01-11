# Monitoring

This project includes Prometheus (and Grafana placeholder) in `docker/docker-compose.yml`.

Prometheus configuration: `docker/prometheus.yml` scrapes the `actuator/prometheus` endpoints of the services:
- http://microservice-produit:8081/actuator/prometheus
- http://microservice-commande:8082/actuator/prometheus
- http://api-gateway:8080/actuator/prometheus

Grafana: available at http://localhost:3001 (admin/admin). Add Prometheus as a data source (http://prometheus:9090).

Notes:
- Ensure services expose `management.endpoints.web.exposure.include` with `prometheus` (already set in service `application.yml`).
- Resilience4j metrics, Micrometer and Actuator provide useful metrics for circuit breakers and retries.
