package com.example.commande.exception;

public class ProduitServiceUnavailableException extends RuntimeException {
    public ProduitServiceUnavailableException(String message) {
        super(message);
    }

    public ProduitServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
