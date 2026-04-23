package fr.epf.restaurant.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente la requête JSON envoyée pour créer une commande fournisseur.
 */
public class CreerCommandeFournisseurRequest {

    private Long fournisseurId;
    private List<LigneCommandeFournisseurRequest> lignes = new ArrayList<>();

    public Long getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(final Long fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public List<LigneCommandeFournisseurRequest> getLignes() {
        return lignes;
    }

    public void setLignes(final List<LigneCommandeFournisseurRequest> lignes) {
        this.lignes = lignes;
    }

    /**
     * Représente une ligne envoyée lors de la création d'une commande fournisseur.
     */
    public static class LigneCommandeFournisseurRequest {
        private Long ingredientId;
        private Double quantite;
        private BigDecimal prixUnitaire;

        public Long getIngredientId() {
            return ingredientId;
        }

        public void setIngredientId(final Long ingredientId) {
            this.ingredientId = ingredientId;
        }

        public Double getQuantite() {
            return quantite;
        }

        public void setQuantite(final Double quantite) {
            this.quantite = quantite;
        }

        public BigDecimal getPrixUnitaire() {
            return prixUnitaire;
        }

        public void setPrixUnitaire(final BigDecimal prixUnitaire) {
            this.prixUnitaire = prixUnitaire;
        }
    }
}
