package fr.epf.restaurant.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente le détail complet d'un plat pour l'API.
 */
public class PlatDetailDto {

    private Long id;
    private String nom;
    private String description;
    private BigDecimal prix;
    private List<PlatIngredientDto> ingredients = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(final String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(final BigDecimal prix) {
        this.prix = prix;
    }

    public List<PlatIngredientDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(final List<PlatIngredientDto> ingredients) {
        this.ingredients = ingredients;
    }
}
