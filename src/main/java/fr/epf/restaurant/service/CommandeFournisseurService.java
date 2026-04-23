package fr.epf.restaurant.service;

import fr.epf.restaurant.dao.CommandeFournisseurDao;
import fr.epf.restaurant.dao.FournisseurDao;
import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest;
import fr.epf.restaurant.exception.DonneesInvalidesException;
import fr.epf.restaurant.exception.RessourceIntrouvableException;
import fr.epf.restaurant.exception.StatutInvalideException;
import fr.epf.restaurant.model.CommandeFournisseur;
import fr.epf.restaurant.model.LigneCommandeFournisseur;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommandeFournisseurService {

    private final CommandeFournisseurDao commandeFournisseurDao;
    private final FournisseurDao fournisseurDao;
    private final IngredientDao ingredientDao;
    private final StockService stockService;

    public CommandeFournisseurService(
            final CommandeFournisseurDao commandeFournisseurDao,
            final FournisseurDao fournisseurDao,
            final IngredientDao ingredientDao,
            final StockService stockService
    ) {
        this.commandeFournisseurDao = commandeFournisseurDao;
        this.fournisseurDao = fournisseurDao;
        this.ingredientDao = ingredientDao;
        this.stockService = stockService;
    }

    /**
     * Retourne toutes les commandes fournisseur, avec filtre optionnel.
     */
    public List<CommandeFournisseur> listerCommandes(final String statut) {
        if (statut == null || statut.isBlank()) {
            return commandeFournisseurDao.findAll();
        }
        return commandeFournisseurDao.findByStatut(statut);
    }

    /**
     * Retourne une commande fournisseur ou lève une exception si elle n'existe pas.
     */
    public CommandeFournisseur getCommandeById(final Long id) {
        return commandeFournisseurDao.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Commande fournisseur introuvable: " + id));
    }

    /**
     * Retourne les lignes d'une commande fournisseur.
     */
    public List<LigneCommandeFournisseur> getLignesCommande(final Long idCommande) {
        return commandeFournisseurDao.findLignesByCommandeId(idCommande);
    }

    @Transactional
    /**
     * Crée une commande fournisseur après validation des données.
     */
    public CommandeFournisseur creerCommande(final CreerCommandeFournisseurRequest request) {
        if (request == null) {
            throw new DonneesInvalidesException("La requete est vide");
        }
        if (request.getFournisseurId() == null) {
            throw new DonneesInvalidesException("fournisseurId obligatoire");
        }
        if (request.getLignes() == null || request.getLignes().isEmpty()) {
            throw new DonneesInvalidesException("Il faut au moins une ligne de commande");
        }

        fournisseurDao.findById(request.getFournisseurId())
                .orElseThrow(() -> new RessourceIntrouvableException(
                        "Fournisseur introuvable: " + request.getFournisseurId()
                ));

        CommandeFournisseur commande = commandeFournisseurDao.save(request.getFournisseurId());

        for (CreerCommandeFournisseurRequest.LigneCommandeFournisseurRequest ligne : request.getLignes()) {
            if (ligne.getIngredientId() == null) {
                throw new DonneesInvalidesException("ingredientId obligatoire");
            }
            if (ligne.getQuantite() == null || ligne.getQuantite() <= 0) {
                throw new DonneesInvalidesException("quantite doit etre > 0");
            }
            if (ligne.getPrixUnitaire() == null || ligne.getPrixUnitaire().signum() < 0) {
                throw new DonneesInvalidesException("prixUnitaire doit etre >= 0");
            }

            ingredientDao.findById(ligne.getIngredientId())
                    .orElseThrow(() -> new RessourceIntrouvableException(
                            "Ingredient introuvable: " + ligne.getIngredientId()
                    ));

            commandeFournisseurDao.addLigne(
                    commande.getId(),
                    ligne.getIngredientId(),
                    ligne.getQuantite(),
                    ligne.getPrixUnitaire()
            );
        }

        return getCommandeById(commande.getId());
    }

    @Transactional
    /**
     * Passe une commande fournisseur au statut ENVOYEE.
     */
    public CommandeFournisseur envoyerCommande(final Long commandeId) {
        CommandeFournisseur commande = getCommandeById(commandeId);
        if (!"EN_ATTENTE".equals(commande.getStatut())) {
            throw new StatutInvalideException("La commande doit etre EN_ATTENTE pour etre envoyee");
        }

        commandeFournisseurDao.updateStatut(commandeId, "ENVOYEE");
        return getCommandeById(commandeId);
    }

    @Transactional
    /**
     * Passe une commande fournisseur au statut RECUE et réapprovisionne le stock.
     */
    public CommandeFournisseur recevoirCommande(final Long commandeId) {
        CommandeFournisseur commande = getCommandeById(commandeId);
        if (!"ENVOYEE".equals(commande.getStatut())) {
            throw new StatutInvalideException("La commande doit etre ENVOYEE pour etre recue");
        }

        stockService.reapprovisionnerApresReception(commandeId);
        commandeFournisseurDao.updateStatut(commandeId, "RECUE");
        return getCommandeById(commandeId);
    }

    @Transactional
    /**
     * Supprime une commande fournisseur.
     */
    public void supprimerCommande(final Long commandeId) {
        getCommandeById(commandeId);
        commandeFournisseurDao.deleteById(commandeId);
    }

    @Transactional
    /**
     * Annule une commande fournisseur si son état le permet.
     */
    public CommandeFournisseur annulerCommande(final Long commandeId) {
        CommandeFournisseur commande = getCommandeById(commandeId);
        if ("RECUE".equals(commande.getStatut())) {
            throw new StatutInvalideException("Une commande RECUE ne peut pas etre annulee");
        }
        if ("ANNULEE".equals(commande.getStatut())) {
            throw new StatutInvalideException("La commande est deja annulee");
        }

        commandeFournisseurDao.updateStatut(commandeId, "ANNULEE");
        return getCommandeById(commandeId);
    }
}
