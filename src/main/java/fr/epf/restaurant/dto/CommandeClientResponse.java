package fr.epf.restaurant.dto;

import fr.epf.restaurant.model.Client;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une commande client complète renvoyée par l'API.
 */
public class CommandeClientResponse {

    private Long id;
    private Client client;
    private LocalDateTime dateCommande;
    private String statut;
    private List<LigneCommandeClientResponse> lignes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(final Client client) {
        this.client = client;
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

    public List<LigneCommandeClientResponse> getLignes() {
        return lignes;
    }

    public void setLignes(final List<LigneCommandeClientResponse> lignes) {
        this.lignes = lignes;
    }
}
