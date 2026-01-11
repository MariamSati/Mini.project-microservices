# Architecture (ASCII diagram)

Frontend (React)
    |
    v
API Gateway (Spring Cloud Gateway) ---> Keycloak (Auth Server)
    |
    v
+--------------------+      +--------------------+
| produit-service    |      | commande-service   |
| (port 8081)        |      | (port 8082)        |
+--------------------+      +--------------------+

Notes:
- All client requests go through API Gateway (port 8080).
- Keycloak provides OAuth2/OpenID Connect and issues JWTs (realm `mini-projet`).
- Services validate JWTs using Keycloak issuer.
- Inter-service calls propagate Authorization bearer token via Gateway or direct internal calls.
