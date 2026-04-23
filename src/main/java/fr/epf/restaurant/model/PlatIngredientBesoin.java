package fr.epf.restaurant.model;

/**
 * Représente la quantité d'un ingrédient nécessaire pour préparer un plat.
 */
public class PlatIngredientBesoin {

    private Long ingredientId;
    private String ingredientNom;
    private String unite;
    private Double stockActuel;
    private Double seuilAlerte;
    private Double quantiteRequise;

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

    public String getUnite() {
        return unite;
    }

    public void setUnite(final String unite) {
        this.unite = unite;
    }

    public Double getStockActuel() {
        return stockActuel;
    }

    public void setStockActuel(final Double stockActuel) {
        this.stockActuel = stockActuel;
    }

    public Double getSeuilAlerte() {
        return seuilAlerte;
    }

    public void setSeuilAlerte(final Double seuilAlerte) {
        this.seuilAlerte = seuilAlerte;
    }

    public Double getQuantiteRequise() {
        return quantiteRequise;
    }

    public void setQuantiteRequise(final Double quantiteRequise) {
        this.quantiteRequise = quantiteRequise;
    }
}
