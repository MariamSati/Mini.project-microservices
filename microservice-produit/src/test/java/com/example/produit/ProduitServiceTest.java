package com.example.produit;

import com.example.produit.entity.Produit;
import com.example.produit.service.ProduitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProduitServiceTest {

    @Autowired
    private ProduitService produitService;

    @Test
    void addAndRetrieveProduit() {
        Produit p = new Produit(null, "Test", "Desc", 9.99, 10);
        Produit saved = produitService.addProduit(p);
        assertThat(saved.getId()).isNotNull();

        var fetched = produitService.getById(saved.getId());
        assertThat(fetched).isPresent();
        assertThat(fetched.get().getNom()).isEqualTo("Test");
    }
}
