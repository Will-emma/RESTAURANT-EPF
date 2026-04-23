package fr.epf.restaurant.dto;

/**
 * Transporte les informations utiles pour afficher une alerte de stock.
 */
public class AlerteStockDto {

    private Long ingredientId;
    private String ingredientNom;
    private String unite;
    private Double stockActuel;
    private Double seuilAlerte;
    private Double quantiteACommander;

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

    public Double getQuantiteACommander() {
        return quantiteACommander;
    }

    public void setQuantiteACommander(final Double quantiteACommander) {
        this.quantiteACommander = quantiteACommander;
    }
}
