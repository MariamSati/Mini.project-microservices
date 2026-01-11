# microservice-commande

Spring Boot microservice which manages orders (commandes).

Endpoints:
- POST /api/commandes (ROLE_CLIENT)
- GET /api/commandes/mes-commandes (ROLE_CLIENT)
- GET /api/commandes (ROLE_ADMIN)

Behavior:
- On creation, verifies products via produit service and decrements stock.
- Propagates JWT to produit-service for stock operations.

Run with docker compose (top-level `docker/docker-compose.yml`).
