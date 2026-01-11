# Setup rapide

1. Build & run all services:

   docker compose -f docker/docker-compose.yml up --build

2. Import realm if not auto-imported:
   - Admin Console: http://localhost:8180/auth (admin/admin)
   - Realm -> Import -> choose `keycloak-config/realm.json`

3. Create test users or use provided ones:
   - admin / admin (ADMIN)
   - client / client (CLIENT)

4. Frontend: http://localhost:3000
   - Login redirects to Keycloak
   - All API calls go to the gateway at http://localhost:8080

Monitoring:
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3001 (admin/admin)

5. For scanning & quality (examples):
   - OWASP Dependency-Check: mvn org.owasp:dependency-check-maven:check
   - Trivy: trivy image <image:tag>
