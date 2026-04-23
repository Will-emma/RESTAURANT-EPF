package fr.epf.restaurant.dto;

import fr.epf.restaurant.model.Plat;

/**
 * Représente une ligne de commande client renvoyée par l'API.
 */
public class LigneCommandeClientResponse {

    private Long id;
    private Long commandeClientId;
    private Plat plat;
    private Integer quantite;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getCommandeClientId() {
        return commandeClientId;
    }

    public void setCommandeClientId(final Long commandeClientId) {
        this.commandeClientId = commandeClientId;
    }

    public Plat getPlat() {
        return plat;
    }

    public void setPlat(final Plat plat) {
        this.plat = plat;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(final Integer quantite) {
        this.quantite = quantite;
    }
}
