package fr.epf.restaurant.model;

/**
 * Représente un ingrédient stocké en cuisine.
 */
public class Ingredient {

    private Long id;
    private String nom;
    private String unite;
    private Double stockActuel;
    private Double seuilAlerte;

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
}
