package fr.epf.restaurant.model;

import java.time.LocalDateTime;

/**
 * Représente l'en-tête d'une commande client.
 */
public class CommandeClient {

    private Long id;
    private Long clientId;
    private LocalDateTime dateCommande;
    private String statut;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(final Long clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(final LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(final String statut) {
        this.statut = statut;
    }
}
