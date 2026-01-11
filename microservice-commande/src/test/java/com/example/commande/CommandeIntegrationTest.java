package com.example.commande;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public class CommandeIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer("postgres:15")
            .withDatabaseName("commande_db")
            .withUsername("commande")
            .withPassword("commande_password");

    @RegisterExtension
    static WireMockExtension wiremock = WireMockExtension.newInstance().options(wireMockConfig().dynamicPort()).build();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createCommande_flow_success() {
        // Stub produit service GET
        wiremock.stubFor(get(urlPathMatching("/api/produits/1"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"nom\":\"P1\",\"description\":\"d\",\"prix\":10.0,\"quantiteStock\":5}")));

        // Stub decrement
        wiremock.stubFor(post(urlPathMatching("/api/produits/1/decrement.*"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"quantiteStock\":3}")));

        // Stub increment (compensation)
        wiremock.stubFor(post(urlPathMatching("/api/produits/1/increment.*"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"quantiteStock\":5}")));

        // Prepare commande payload
        var payload = Map.of("lignes", List.of(Map.of("idProduit", 1, "quantite", 2)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("test-token");

        HttpEntity<Object> request = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/api/commandes", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("montantTotal");
    }
}
