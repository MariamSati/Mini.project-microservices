package com.example.commande.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ProduitClientExtended {
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(ProduitClientExtended.class);

    public ProduitClientExtended(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://microservice-produit:8081").build();
    }

    public Map<String, Object> incrementStock(Long id, int quantite) {
        try {
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/api/produits/{id}/increment").queryParam("quantite", quantite).build(id))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            logger.error("Error incrementing stock for produit {}: {}", id, e.getMessage());
            return null;
        }
    }
}
