# CI & Integration Tests

- CI pipeline located at `.github/workflows/ci.yml` builds backend modules, runs unit and integration tests, and performs OWASP dependency-check.
- SonarQube scan is present as a placeholder; to enable, set a `SONAR_TOKEN` secret in the repository and enable the job.

Integration tests:
- `microservice-commande` contains `CommandeIntegrationTest` using Testcontainers (PostgreSQL) and WireMock to simulate the `produit` service.
- Tests run in CI and validate the end-to-end creation of a commande (product fetch + decrement + persistence).

Security in tests:
- For rapid CI, tests use a test profile with a `service.token` and do not validate Keycloak in WireMock.
- For end-to-end OAuth tests, one can add a Keycloak Testcontainer and obtain tokens programmatically.
