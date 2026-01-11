# microservice-produit

Spring Boot microservice exposing CRUD operations for `Produit`.

Endpoints:
- POST /api/produits (ROLE_ADMIN)
- PUT /api/produits/{id} (ROLE_ADMIN)
- DELETE /api/produits/{id} (ROLE_ADMIN)
- GET /api/produits (ROLE_ADMIN | ROLE_CLIENT)
- GET /api/produits/{id} (ROLE_ADMIN | ROLE_CLIENT)

Run (docker compose):
- via top-level `docker/docker-compose.yml`

Security:
- Resource server configured with Keycloak issuer at `http://keycloak:8080/realms/myapp-realm`.
- Roles are mapped from `realm_access.roles` to Spring `ROLE_XXX`.

