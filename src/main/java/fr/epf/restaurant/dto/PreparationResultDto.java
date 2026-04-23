package fr.epf.restaurant.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente le résultat renvoyé après la préparation d'une commande client.
 */
public class PreparationResultDto {

    private Object commande;
    private List<AlerteStockDto> alertes = new ArrayList<>();

    public Object getCommande() {
        return commande;
    }

    public void setCommande(final Object commande) {
        this.commande = commande;
    }

    public List<AlerteStockDto> getAlertes() {
        return alertes;
    }

    public void setAlertes(final List<AlerteStockDto> alertes) {
        this.alertes = alertes;
    }
}
