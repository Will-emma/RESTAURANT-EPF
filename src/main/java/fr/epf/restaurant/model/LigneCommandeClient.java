package fr.epf.restaurant.model;

/**
 * Représente une ligne d'une commande client.
 */
public class LigneCommandeClient {

    private Long id;
    private Long commandeClientId;
    private Long platId;
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

    public Long getPlatId() {
        return platId;
    }

    public void setPlatId(final Long platId) {
        this.platId = platId;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(final Integer quantite) {
        this.quantite = quantite;
    }
}
