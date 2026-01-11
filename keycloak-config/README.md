# Keycloak configuration

This folder contains an exportable Keycloak realm (`realm.json`) for realm `mini-projet`.

Clients created in this realm:
- frontend (public, redirect URI http://localhost:3000/*)
- backend-services (confidential, secret `backend-secret`)

Users:
- admin / admin (realm role ADMIN)
- client / client (realm role CLIENT)

Import:
- You can import `realm.json` when starting Keycloak with `--import-realm` or via the Admin Console (Realm -> Import).

Note: To ensure tokens have an `aud` claim like `myapp`, add an Audience Mapper to the client in the Admin Console: Clients -> (select client) -> Client Scopes or Mappers -> Add -> "Audience" mapper -> Set Included Client Audience to "myapp" and add to ID token / access token if required.

Docker snip (example):
```
keycloak:
  image: quay.io/keycloak/keycloak:latest
  environment:
    KEYCLOAK_ADMIN: admin
    KEYCLOAK_ADMIN_PASSWORD: admin
  command: start-dev --import-realm
  ports:
    - 8180:8080
  volumes:
    - ./keycloak-config:/opt/keycloak/data/import:ro
```
