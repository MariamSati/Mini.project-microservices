package com.example.commande.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ProduitClient {
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(ProduitClient.class);

    public ProduitClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://microservice-produit:8081").build();
    }

    public Map<String, Object> getProduitById(Long id) {
        String token = extractToken();
        try {
            return webClient.get()
                    .uri("/api/produits/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            logger.error("Error fetching produit {}: {}", id, e.getMessage());
            return null;
        }
    }

    public Map<String, Object> decrementStock(Long id, int quantite) {
        String token = extractToken();
        try {
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/api/produits/{id}/decrement").queryParam("quantite", quantite).build(id))
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            logger.error("Error decrementing stock for produit {}: {}", id, e.getMessage());
            return null;
        }
    }

    private String extractToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return "Bearer " + jwt.getTokenValue();
        }
        logger.warn("No JWT found in SecurityContext when calling Produit service");
        return "";
    }
}
