package fr.epf.restaurant.dto;

import fr.epf.restaurant.model.Fournisseur;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une commande fournisseur complète renvoyée par l'API.
 */
public class CommandeFournisseurResponse {

    private Long id;
    private Fournisseur fournisseur;
    private LocalDateTime dateCommande;
    private String statut;
    private List<LigneCommandeFournisseurResponse> lignes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(final Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
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

    public List<LigneCommandeFournisseurResponse> getLignes() {
        return lignes;
    }

    public void setLignes(final List<LigneCommandeFournisseurResponse> lignes) {
        this.lignes = lignes;
    }
}
