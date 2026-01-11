package com.example.commande.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ProduitClientV2 {
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(ProduitClientV2.class);

    public ProduitClientV2(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://microservice-produit:8081").build();
    }

    public Map<String, Object> getProduitById(Long id) {
        try {
            return webClient.get()
                    .uri("/api/produits/{id}", id)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            logger.error("Erreur fetching produit {}: {}", id, e.getMessage());
            throw e;
        }
    }

    public Map<String, Object> decrementStock(Long id, int quantite) {
        try {
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/api/produits/{id}/decrement").queryParam("quantite", quantite).build(id))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            logger.error("Error decrementing stock for produit {}: {}", id, e.getMessage());
            throw e;
        }
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
            throw e;
        }
    }
}
