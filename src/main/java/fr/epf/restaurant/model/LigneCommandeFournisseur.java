package fr.epf.restaurant.model;

import java.math.BigDecimal;

/**
 * Représente une ligne d'une commande fournisseur.
 */
public class LigneCommandeFournisseur {

    private Long id;
    private Long commandeFournisseurId;
    private Long ingredientId;
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

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(final Long ingredientId) {
        this.ingredientId = ingredientId;
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
