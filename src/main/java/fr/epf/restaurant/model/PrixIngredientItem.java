package fr.epf.restaurant.model;

import java.math.BigDecimal;

/**
 * Représente un prix fournisseur pour un ingrédient donné.
 */
public class PrixIngredientItem {

    private Long fournisseurId;
    private String fournisseurNom;
    private BigDecimal prixUnitaire;

    public Long getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(final Long fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public String getFournisseurNom() {
        return fournisseurNom;
    }

    public void setFournisseurNom(final String fournisseurNom) {
        this.fournisseurNom = fournisseurNom;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(final BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
}
