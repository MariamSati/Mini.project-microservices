# Secrets and Environment Variables

This project uses environment variables for sensitive values. Do NOT commit real secrets to git.

Recommended steps:

1. Copy `docker/.env.example` to `docker/.env` and replace `change_me` with secure values.
2. Start services with: `docker compose --env-file docker/.env up --build`.
3. For CI, store secrets in your CI provider (GitHub Actions Secrets) and reference them in workflows.
4. For production, prefer a secret manager (Vault, AWS Secrets Manager) and inject secrets during deployment.

Notes:
- Keycloak client secrets and admin credentials must be kept secure. The `keycloak-config/realm.json` used in demos contains test secrets and should be replaced in prod.
