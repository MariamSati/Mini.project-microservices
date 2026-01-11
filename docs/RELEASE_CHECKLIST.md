# Release checklist

Steps to prepare a production release:

- [ ] Replace Keycloak `realm.json` with production configuration; remove hard-coded secrets.
- [ ] Ensure all secrets (DB passwords, Keycloak admin, client secrets) are stored in a secret manager and not in repo.
- [ ] Configure Sonar Cloud and set `SONAR_TOKEN` in CI secrets.
- [ ] Add monitoring dashboards (Grafana) and alerts for service errors, high latency, low stock thresholds.
- [ ] Use TLS termination (nginx/ingress) in front of gateway and Keycloak in production.
- [ ] Enable backups for Postgres volumes and configure retention.
- [ ] Perform security scans (Trivy for images, OWASP dependency-check is already included in CI).
- [ ] Run full integration tests and smoke tests against staging environment.
- [ ] Update deployment documentation and runbook.
