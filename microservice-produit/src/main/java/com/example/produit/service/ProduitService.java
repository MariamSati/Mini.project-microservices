package com.example.produit.service;

import com.example.produit.entity.Produit;
import com.example.produit.repository.ProduitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {
    private final ProduitRepository produitRepository;
    private final Logger logger = LoggerFactory.getLogger(ProduitService.class);

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public Produit addProduit(Produit p) {
        logger.info("Adding produit {}", p.getNom());
        return produitRepository.save(p);
    }

    public Produit updateProduit(Long id, Produit p) {
        logger.info("Updating produit id={}", id);
        p.setId(id);
        return produitRepository.save(p);
    }

    public void deleteProduit(Long id) {
        logger.info("Deleting produit id={}", id);
        produitRepository.deleteById(id);
    }

    public List<Produit> listAll() {
        return produitRepository.findAll();
    }

    public Optional<Produit> getById(Long id) {
        return produitRepository.findById(id);
    }
}
