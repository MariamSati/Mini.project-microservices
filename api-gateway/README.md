# api-gateway

Spring Cloud Gateway configured as OAuth2 resource server validating JWT from Keycloak (realm `mini-projet`).

Routes:
- /api/produits/** -> http://microservice-produit:8081
- /api/commandes/** -> http://microservice-commande:8082

Expose port 8080 as application entry point.

Security: JWT validation via `spring.security.oauth2.resourceserver.jwt.issuer-uri`.
