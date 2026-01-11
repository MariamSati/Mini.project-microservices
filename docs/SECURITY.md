# DevSecOps Placeholders

- SonarQube: service included as placeholder in `docker/docker-compose.yml` (see `sonarqube` service). Configure database and runner in CI to analyze Maven/JS projects.
- OWASP Dependency-Check: Add `org.owasp:dependency-check-maven` to `pom.xml` as a check in CI.
- Trivy: run `trivy fs <project-dir>` to scan images and filesystem.

Suggested commands:
- mvn -q -DskipTests org.owasp:dependency-check-maven:check
- trivy image <image:tag>
- docker compose -f docker/docker-compose.yml up -d sonarqube
