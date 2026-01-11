package com.example.commande.controller;

import com.example.commande.entity.Commande;
import com.example.commande.service.CommandeService;
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
@RequestMapping("/api/commandes")
public class CommandeController {
    private final CommandeService commandeService;
    private final Logger logger = LoggerFactory.getLogger(CommandeController.class);

    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> createCommande(@RequestBody Commande c, @AuthenticationPrincipal Jwt jwt) {
        logger.info("User {} creating commande", jwt.getSubject());
        var saved = commandeService.createCommande(c);
        return ResponseEntity.created(URI.create("/api/commandes/" + saved.getId())).body(saved);
    }

    @GetMapping("/mes-commandes")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<Commande>> getMesCommandes(@AuthenticationPrincipal Jwt jwt) {
        logger.info("User {} retrieving their commandes", jwt.getSubject());
        return ResponseEntity.ok(commandeService.getMesCommandes());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Commande>> getAll(@AuthenticationPrincipal Jwt jwt) {
        logger.info("Admin {} retrieving all commandes", jwt.getSubject());
        return ResponseEntity.ok(commandeService.getAllCommandes());
    }
}
