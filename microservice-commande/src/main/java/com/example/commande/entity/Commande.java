package com.example.commande.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "commandes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateCommande;

    private String statut; // e.g., PENDING

    private double montantTotal;

    private String userId; // stored from JWT subject/pref_username

    @ElementCollection
    @CollectionTable(name = "ligne_commandes", joinColumns = @JoinColumn(name = "commande_id"))
    private List<LigneCommande> lignes;
}
