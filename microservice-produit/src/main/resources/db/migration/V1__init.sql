CREATE TABLE produit (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    description TEXT,
    prix NUMERIC(12,2) NOT NULL,
    quantite_stock INTEGER NOT NULL
);
