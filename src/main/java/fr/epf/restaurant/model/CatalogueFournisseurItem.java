package fr.epf.restaurant.model;

import java.math.BigDecimal;

/**
 * Représente un prix d'ingrédient dans le catalogue d'un fournisseur.
 */
public class CatalogueFournisseurItem {

    private Long ingredientId;
    private String ingredientNom;
    private String ingredientUnite;
    private BigDecimal prixUnitaire;

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(final Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientNom() {
        return ingredientNom;
    }

    public void setIngredientNom(final String ingredientNom) {
        this.ingredientNom = ingredientNom;
    }

    public String getIngredientUnite() {
        return ingredientUnite;
    }

    public void setIngredientUnite(final String ingredientUnite) {
        this.ingredientUnite = ingredientUnite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(final BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
}
