# Documentation

Start:
1. Build and run all services with:

   docker compose -f docker/docker-compose.yml up --build

2. Keycloak will be available at http://localhost:8180/auth (admin/admin)
   - Import `keycloak-config/realm.json` if not auto-imported
   - Realm: `mini-projet`
   - Clients: `frontend` (public), `backend-services` (confidential)
   - Users: `admin` / `admin` (ADMIN), `client` / `client` (CLIENT)

3. Frontend will be at http://localhost:3000

Notes:
- Gateway validates JWTs and forwards the Authorization header to services.
- Inter-service calls propagate JWT so the products service can verify request author.
