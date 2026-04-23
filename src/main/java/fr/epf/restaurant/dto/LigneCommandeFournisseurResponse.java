package fr.epf.restaurant.dto;

import fr.epf.restaurant.model.Ingredient;

import java.math.BigDecimal;

/**
 * Représente une ligne de commande fournisseur renvoyée par l'API.
 */
public class LigneCommandeFournisseurResponse {

    private Long id;
    private Long commandeFournisseurId;
    private Ingredient ingredient;
    private Double quantiteCommandee;
    private BigDecimal prixUnitaire;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getCommandeFournisseurId() {
        return commandeFournisseurId;
    }

    public void setCommandeFournisseurId(final Long commandeFournisseurId) {
        this.commandeFournisseurId = commandeFournisseurId;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(final Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Double getQuantiteCommandee() {
        return quantiteCommandee;
    }

    public void setQuantiteCommandee(final Double quantiteCommandee) {
        this.quantiteCommandee = quantiteCommandee;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(final BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
}
