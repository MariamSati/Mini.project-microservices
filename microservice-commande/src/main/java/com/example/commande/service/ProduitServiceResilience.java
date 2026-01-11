package com.example.commande.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ProduitServiceResilience {
    private final ProduitClient produitClient;
    private final ProduitClientV2 produitClientV2;
    private final Logger logger = LoggerFactory.getLogger(ProduitServiceResilience.class);

    public ProduitServiceResilience(ProduitClient produitClient, ProduitClientV2 produitClientV2) {
        this.produitClient = produitClient;
        this.produitClientV2 = produitClientV2;
    }

    @io.github.resilience4j.retry.annotation.Retry(name = "produitRetry", fallbackMethod = "fallbackGetProduit")
    @CircuitBreaker(name = "produitService", fallbackMethod = "fallbackGetProduit")
    public Map<String, Object> getProduitById(Long id) {
        return produitClient.getProduitById(id);
    }

    private Map<String, Object> fallbackGetProduit(Long id, Throwable ex) {
        logger.warn("Fallback getProduitById({}) due to {}", id, ex.toString());
        throw new com.example.commande.exception.ProduitServiceUnavailableException("Produit service unavailable", ex);
    }

    @io.github.resilience4j.retry.annotation.Retry(name = "produitRetry", fallbackMethod = "fallbackDecrement")
    @CircuitBreaker(name = "produitService", fallbackMethod = "fallbackDecrement")
    public Map<String, Object> decrementStock(Long id, int q) {
        return produitClient.decrementStock(id, q);
    }

    private Map<String, Object> fallbackDecrement(Long id, int q, Throwable ex) {
        logger.warn("Fallback decrement for produit {} q={} due to {}", id, q, ex.toString());
        throw new com.example.commande.exception.ProduitServiceUnavailableException("Produit service unavailable during decrement", ex);
    }

    public Map<String, Object> incrementStock(Long id, int q) {
        // no retry for compensation, just call
        try {
            return produitClientV2.incrementStock(id, q);
        } catch (Exception e) {
            logger.error("Compensation increment failed for produit {}: {}", id, e.getMessage());
            return null;
        }
    }
}
