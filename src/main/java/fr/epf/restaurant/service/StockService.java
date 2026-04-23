package fr.epf.restaurant.service;

import fr.epf.restaurant.dao.CommandeClientDao;
import fr.epf.restaurant.dao.CommandeFournisseurDao;
import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dao.PlatDao;
import fr.epf.restaurant.dto.AlerteStockDto;
import fr.epf.restaurant.dto.RecommandationDto;
import fr.epf.restaurant.exception.DonneesInvalidesException;
import fr.epf.restaurant.exception.RessourceIntrouvableException;
import fr.epf.restaurant.exception.StockInsuffisantException;
import fr.epf.restaurant.model.Ingredient;
import fr.epf.restaurant.model.LigneCommandeClient;
import fr.epf.restaurant.model.LigneCommandeFournisseur;
import fr.epf.restaurant.model.PlatIngredientBesoin;
import fr.epf.restaurant.model.PrixIngredientItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockService {

    private final IngredientDao ingredientDao;
    private final PlatDao platDao;
    private final CommandeClientDao commandeClientDao;
    private final CommandeFournisseurDao commandeFournisseurDao;

    public StockService(
            final IngredientDao ingredientDao,
            final PlatDao platDao,
            final CommandeClientDao commandeClientDao,
            final CommandeFournisseurDao commandeFournisseurDao
    ) {
        this.ingredientDao = ingredientDao;
        this.platDao = platDao;
        this.commandeClientDao = commandeClientDao;
        this.commandeFournisseurDao = commandeFournisseurDao;
    }

    @Transactional
    /**
     * Vérifie puis consomme le stock nécessaire à une commande client.
     */
    public void consommerStockPourCommandeClient(final Long commandeId) {
        // On calcule d'abord tous les besoins pour éviter de décrémenter le stock partiellement.
        Map<Long, Double> besoins = calculerBesoinsIngredientsCommandeClient(commandeId);

        for (Map.Entry<Long, Double> entree : besoins.entrySet()) {
            Long ingredientId = entree.getKey();
            Double quantiteNecessaire = entree.getValue();

            Ingredient ingredient = ingredientDao.findById(ingredientId)
                    .orElseThrow(() -> new RessourceIntrouvableException("Ingredient introuvable: " + ingredientId));

            if (ingredient.getStockActuel() < quantiteNecessaire) {
                String message = "Stock insuffisant pour " + ingredient.getNom()
                        + " (requis=" + quantiteNecessaire
                        + ", stock=" + ingredient.getStockActuel() + ")";
                throw new StockInsuffisantException(message);
            }
        }

        // Si tout est bon, on applique ensuite les décrémentations.
        for (Map.Entry<Long, Double> entree : besoins.entrySet()) {
            ingredientDao.decrementStock(entree.getKey(), entree.getValue());
        }
    }

    @Transactional
    /**
     * Rend le stock consommé par une commande client.
     */
    public void restituerStockPourCommandeClient(final Long commandeId) {
        Map<Long, Double> besoins = calculerBesoinsIngredientsCommandeClient(commandeId);
        for (Map.Entry<Long, Double> entree : besoins.entrySet()) {
            ingredientDao.incrementStock(entree.getKey(), entree.getValue());
        }
    }

    @Transactional
    /**
     * Ajoute au stock les quantités reçues dans une commande fournisseur.
     */
    public void reapprovisionnerApresReception(final Long commandeFournisseurId) {
        List<LigneCommandeFournisseur> lignes = commandeFournisseurDao.findLignesByCommandeId(commandeFournisseurId);
        for (LigneCommandeFournisseur ligne : lignes) {
            ingredientDao.incrementStock(ligne.getIngredientId(), ligne.getQuantiteCommandee());
        }
    }

    /**
     * Retourne les alertes de stock à afficher.
     */
    public List<AlerteStockDto> getAlertesStock() {
        List<Ingredient> ingredients = ingredientDao.findIngredientsSousAlerte();
        List<AlerteStockDto> alertes = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            AlerteStockDto alerte = new AlerteStockDto();
            // Le DTO garde les infos utiles pour afficher rapidement l'alerte côté front.
            alerte.setIngredientId(ingredient.getId());
            alerte.setIngredientNom(ingredient.getNom());
            alerte.setUnite(ingredient.getUnite());
            alerte.setStockActuel(ingredient.getStockActuel());
            alerte.setSeuilAlerte(ingredient.getSeuilAlerte());
            alerte.setQuantiteACommander(ingredient.getSeuilAlerte() - ingredient.getStockActuel());
            alertes.add(alerte);
        }

        return alertes;
    }

    /**
     * Retourne la recommandation d'achat pour un ingrédient.
     */
    public RecommandationDto getRecommandationPourIngredient(final Long ingredientId) {
        Ingredient ingredient = ingredientDao.findById(ingredientId)
                .orElseThrow(() -> new RessourceIntrouvableException("Ingredient introuvable: " + ingredientId));

        List<PrixIngredientItem> prix = ingredientDao.findPrixParIngredient(ingredientId);
        if (prix.isEmpty()) {
            throw new DonneesInvalidesException("Aucun prix fournisseur pour l'ingredient " + ingredientId);
        }

        PrixIngredientItem meilleurPrix = prix.get(0);
        RecommandationDto dto = new RecommandationDto();
        dto.setIngredientId(ingredientId);
        dto.setFournisseurId(meilleurPrix.getFournisseurId());
        dto.setFournisseurNom(meilleurPrix.getFournisseurNom());
        dto.setPrixUnitaire(meilleurPrix.getPrixUnitaire());
        dto.setQuantiteRecommandee(calculerQuantiteRecommandee(ingredient));
        return dto;
    }

    /**
     * Calcule la quantité totale nécessaire pour chaque ingrédient d'une commande client.
     */
    private Map<Long, Double> calculerBesoinsIngredientsCommandeClient(final Long commandeId) {
        List<LigneCommandeClient> lignes = commandeClientDao.findLignesByCommandeId(commandeId);
        Map<Long, Double> besoins = new HashMap<>();

        for (LigneCommandeClient ligne : lignes) {
            // Un même ingrédient peut être utilisé par plusieurs plats, donc on cumule les quantités.
            List<PlatIngredientBesoin> besoinsPlat = platDao.findIngredientsByPlatId(ligne.getPlatId());
            for (PlatIngredientBesoin besoinPlat : besoinsPlat) {
                double quantite = besoinPlat.getQuantiteRequise() * ligne.getQuantite();
                besoins.merge(
                        besoinPlat.getIngredientId(),
                        quantite,
                        (quantiteCumulee, quantiteAjoutee) -> quantiteCumulee + quantiteAjoutee
                );
            }
        }

        return besoins;
    }

    /**
     * Applique la formule du sujet pour calculer la quantité recommandée.
     */
    private Double calculerQuantiteRecommandee(final Ingredient ingredient) {
        if (ingredient.getSeuilAlerte() > ingredient.getStockActuel()) {
            return 2 * (ingredient.getSeuilAlerte() - ingredient.getStockActuel());
        }
        return ingredient.getSeuilAlerte();
    }
}
