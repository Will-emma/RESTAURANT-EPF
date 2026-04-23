package fr.epf.restaurant.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente la requête JSON envoyée pour créer une commande client.
 */
public class CreerCommandeClientRequest {

    private Long clientId;
    private List<LigneCommandeClientRequest> lignes = new ArrayList<>();

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(final Long clientId) {
        this.clientId = clientId;
    }

    public List<LigneCommandeClientRequest> getLignes() {
        return lignes;
    }

    public void setLignes(final List<LigneCommandeClientRequest> lignes) {
        this.lignes = lignes;
    }

    /**
     * Représente une ligne envoyée lors de la création d'une commande client.
     */
    public static class LigneCommandeClientRequest {
        private Long platId;
        private Integer quantite;

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
}
