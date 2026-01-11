# Mini Projet Microservices — Spring Boot + React + Keycloak

This repository contains a sample secure microservices architecture with:
- Spring Boot microservices (produit, commande)
- API Gateway
- React frontend (Keycloak-protected)
- Keycloak realm config (`keycloak-config/realm.json`)
- PostgreSQL databases (one per service)
- Docker Compose for local orchestration
- Flyway DB migrations, monitoring (Prometheus + Grafana), Resilience4j, and CI pipelines

Quick start:
1. Copy `docker/.env.example` to `docker/.env` and set secure values.
2. Launch with: `docker compose --env-file docker/.env -f docker/docker-compose.yml up --build`.

Testing:
- Backend tests: `mvn test` inside each backend module
- Frontend tests: `npm test` inside `frontend-react`

Notes and next steps:
- Replace demo secrets with production-grade secrets and use a secret manager.
- Configure Sonar and set `SONAR_TOKEN` in CI secrets to enable SonarQube analysis.
- See `docs/RELEASE_CHECKLIST.md` for production hardening steps.
