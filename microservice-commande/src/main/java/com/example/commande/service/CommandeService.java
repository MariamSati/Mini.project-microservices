package com.example.commande.service;

import com.example.commande.entity.Commande;
import com.example.commande.entity.LigneCommande;
import com.example.commande.repository.CommandeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final ProduitServiceResilience produitServiceResilience;
    private final Logger logger = LoggerFactory.getLogger(CommandeService.class);

    public CommandeService(CommandeRepository commandeRepository, ProduitServiceResilience produitServiceResilience) {
        this.commandeRepository = commandeRepository;
        this.produitServiceResilience = produitServiceResilience;
    }

    @Transactional
    public Commande createCommande(Commande c) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = (auth != null && auth.getPrincipal() instanceof Jwt jwt) ? jwt.getSubject() : "anonymous";
        c.setUserId(userId);
        c.setDateCommande(LocalDate.now());
        c.setStatut("PENDING");

        double total = 0.0;
        for (LigneCommande ligne : c.getLignes()) {
            Map<String, Object> produit = produitServiceResilience.getProduitById(ligne.getIdProduit());
            if (produit == null || produit.isEmpty()) {
                throw new IllegalArgumentException("Produit not found: " + ligne.getIdProduit());
            }
            int stock = (int) (double) ((Number) produit.getOrDefault("quantiteStock", 0)).doubleValue();
            double prix = ((Number) produit.getOrDefault("prix", 0)).doubleValue();
            if (stock < ligne.getQuantite()) {
                throw new IllegalStateException("Stock insuffisant pour produit " + ligne.getIdProduit());
            }
            ligne.setPrix(prix);

            // Décrémenter le stock via le produit service
            var resp = produitServiceResilience.decrementStock(ligne.getIdProduit(), ligne.getQuantite());
            if (resp == null) {
                throw new IllegalStateException("Erreur lors du décrément de stock pour produit " + ligne.getIdProduit());
            }

            total += prix * ligne.getQuantite();
        }
        c.setMontantTotal(total);
        Commande saved;
        try {
            saved = commandeRepository.save(c);
            logger.info("User {} created commande {} amount={}", userId, saved.getId(), saved.getMontantTotal());
            return saved;
        } catch (Exception e) {
            // Compensation: rollback decremented stock
            logger.error("Error saving commande, attempting compensation: {}", e.getMessage());
            for (LigneCommande ligne : c.getLignes()) {
                try {
                    produitServiceResilience.incrementStock(ligne.getIdProduit(), ligne.getQuantite());
                } catch (Exception ex) {
                    logger.error("Compensation failed for produit {}: {}", ligne.getIdProduit(), ex.getMessage());
                }
            }
            throw e;
        }
    }

    public List<Commande> getMesCommandes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = (auth != null && auth.getPrincipal() instanceof Jwt jwt) ? jwt.getSubject() : "anonymous";
        return commandeRepository.findByUserId(userId);
    }

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }
}
