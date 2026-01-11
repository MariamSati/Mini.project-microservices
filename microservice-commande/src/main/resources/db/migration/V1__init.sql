CREATE TABLE commande (
    id BIGSERIAL PRIMARY KEY,
    montant_total NUMERIC(12,2),
    date_commande TIMESTAMP
);

CREATE TABLE ligne_commande (
    commande_id BIGINT NOT NULL,
    id_produit BIGINT NOT NULL,
    quantite INTEGER NOT NULL
);
