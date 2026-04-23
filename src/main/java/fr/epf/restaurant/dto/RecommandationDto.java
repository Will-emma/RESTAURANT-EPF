package fr.epf.restaurant.dto;

import java.math.BigDecimal;

/**
 * Transporte la recommandation d'achat pour un ingrédient.
 */
public class RecommandationDto {

    private Long ingredientId;
    private Long fournisseurId;
    private String fournisseurNom;
    private BigDecimal prixUnitaire;
    private Double quantiteRecommandee;

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(final Long ingredientId) {
        this.ingredientId = ingredientId;
    }

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

    public Double getQuantiteRecommandee() {
        return quantiteRecommandee;
    }

    public void setQuantiteRecommandee(final Double quantiteRecommandee) {
        this.quantiteRecommandee = quantiteRecommandee;
    }
}
