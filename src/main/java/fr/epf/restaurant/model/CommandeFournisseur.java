package fr.epf.restaurant.model;

import java.time.LocalDateTime;

/**
 * Représente l'en-tête d'une commande fournisseur.
 */
public class CommandeFournisseur {

    private Long id;
    private Long fournisseurId;
    private LocalDateTime dateCommande;
    private String statut;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(final Long fournisseurId) {
        this.fournisseurId = fournisseurId;
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
