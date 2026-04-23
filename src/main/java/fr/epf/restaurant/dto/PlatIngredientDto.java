package fr.epf.restaurant.dto;

import fr.epf.restaurant.model.Ingredient;

/**
 * Représente un ingrédient avec sa quantité requise dans un plat.
 */
public class PlatIngredientDto {

    private Ingredient ingredient;
    private Double quantiteRequise;

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(final Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Double getQuantiteRequise() {
        return quantiteRequise;
    }

    public void setQuantiteRequise(final Double quantiteRequise) {
        this.quantiteRequise = quantiteRequise;
    }
}
