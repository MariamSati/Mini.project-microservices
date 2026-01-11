package com.example.produit.controller;

import com.example.produit.entity.Produit;
import com.example.produit.service.ProduitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {
    private final ProduitService produitService;
    private final Logger logger = LoggerFactory.getLogger(ProduitController.class);

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produit> addProduit(@RequestBody Produit p, @AuthenticationPrincipal Jwt jwt) {
        logger.info("User {} creating produit", jwt.getSubject());
        Produit saved = produitService.addProduit(p);
        return ResponseEntity.created(URI.create("/api/produits/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produit> updateProduit(@PathVariable Long id, @RequestBody Produit p, @AuthenticationPrincipal Jwt jwt) {
        logger.info("User {} updating produit {}", jwt.getSubject(), id);
        Produit updated = produitService.updateProduit(id, p);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        logger.info("User {} deleting produit {}", jwt.getSubject(), id);
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<List<Produit>> listAll(@AuthenticationPrincipal Jwt jwt) {
        logger.info("User {} listing produits", jwt.getSubject());
        return ResponseEntity.ok(produitService.listAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<Produit> getById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        logger.info("User {} getting produit {}", jwt.getSubject(), id);
        return produitService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/decrement")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<Produit> decrementStock(@PathVariable Long id, @RequestParam int quantite, @AuthenticationPrincipal Jwt jwt) {
        logger.info("User {} decrement produit {} by {}", jwt.getSubject(), id, quantite);
        var produitOpt = produitService.getById(id);
        if (produitOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Produit p = produitOpt.get();
        if (p.getQuantiteStock() < quantite) {
            return ResponseEntity.badRequest().build();
        }
        p.setQuantiteStock(p.getQuantiteStock() - quantite);
        produitService.updateProduit(id, p);
        return ResponseEntity.ok(p);
    }

    @PostMapping("/{id}/increment")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<Produit> incrementStock(@PathVariable Long id, @RequestParam int quantite, @AuthenticationPrincipal Jwt jwt) {
        logger.info("User {} increment produit {} by {}", jwt.getSubject(), id, quantite);
        var produitOpt = produitService.getById(id);
        if (produitOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Produit p = produitOpt.get();
        p.setQuantiteStock(p.getQuantiteStock() + quantite);
        produitService.updateProduit(id, p);
        return ResponseEntity.ok(p);
    }
}
