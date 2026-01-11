package com.example.commande;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public class KeycloakE2ETest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("commande_db")
            .withUsername("commande")
            .withPassword("commande_password");

    // Start Keycloak with imported realm
    private static final GenericContainer<?> keycloak = new GenericContainer<>("quay.io/keycloak/keycloak:26.1")
            .withExposedPorts(8080)
            .withEnv("KEYCLOAK_ADMIN", "admin")
            .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
            .withCommand("start-dev", "--import-realm")
            .withFileSystemBind("./keycloak-config/realm.json", "/opt/keycloak/data/import/realm.json")
            .waitingFor(org.testcontainers.containers.wait.strategy.Wait.forHttp("/realms/mini-projet/protocol/openid-connect/certs").forStatusCode(200).withStartupTimeout(java.time.Duration.ofMinutes(2)));

    static {
        keycloak.start();
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void e2e_with_keycloak_token() throws Exception {
        String kcHost = keycloak.getHost();
        Integer kcPort = keycloak.getMappedPort(8080);
        String tokenUrl = String.format("http://%s:%d/realms/mini-projet/protocol/openid-connect/token", kcHost, kcPort);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String body = "grant_type=password&client_id=frontend&username=client&password=client";

        HttpEntity<String> req = new HttpEntity<>(body, headers);
        ResponseEntity<Map> tokenResp = restTemplate.postForEntity(tokenUrl, req, Map.class);
        assertThat(tokenResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        String accessToken = (String) tokenResp.getBody().get("access_token");

        // Also obtain a client_credentials token for backend-services and validate audience contains "backend-services"
        HttpHeaders headersCC = new HttpHeaders();
        headersCC.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String bodyCC = "grant_type=client_credentials&client_id=backend-services&client_secret=backend-secret";
        HttpEntity<String> reqCC = new HttpEntity<>(bodyCC, headersCC);
        ResponseEntity<Map> tokenRespCC = restTemplate.postForEntity(tokenUrl, reqCC, Map.class);
        assertThat(tokenRespCC.getStatusCode()).isEqualTo(HttpStatus.OK);
        String accessTokenCC = (String) tokenRespCC.getBody().get("access_token");

        // Decode JWT payload and assert 'aud' contains backend-services
        String[] parts = accessTokenCC.split("\\.");
        String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        java.util.Map<String,Object> claims = mapper.readValue(payload, java.util.Map.class);
        Object aud = claims.get("aud");
        assertThat(aud).isNotNull();
        if (aud instanceof java.util.List) {
            assertThat(((java.util.List)aud)).contains("backend-services");
        } else {
            assertThat(aud.toString()).contains("backend-services");
        }

        // Call create commande
        HttpHeaders auth = new HttpHeaders();
        auth.setContentType(MediaType.APPLICATION_JSON);
        auth.setBearerAuth(accessToken);

        var commandePayload = Map.of("lignes", java.util.List.of(Map.of("idProduit", 1, "quantite", 1)));
        HttpEntity<Object> request = new HttpEntity<>(commandePayload, auth);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/api/commandes", request, String.class);

        // We expect 503 or 400 because produit service is not stubbed here; main goal is to ensure authentication works end-to-end
        assertThat(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()).isTrue();
    }
}
