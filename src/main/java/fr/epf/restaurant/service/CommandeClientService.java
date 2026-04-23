package fr.epf.restaurant.service;

import fr.epf.restaurant.dao.ClientDao;
import fr.epf.restaurant.dao.CommandeClientDao;
import fr.epf.restaurant.dao.PlatDao;
import fr.epf.restaurant.dto.CreerCommandeClientRequest;
import fr.epf.restaurant.dto.PreparationResultDto;
import fr.epf.restaurant.exception.DonneesInvalidesException;
import fr.epf.restaurant.exception.RessourceIntrouvableException;
import fr.epf.restaurant.exception.StatutInvalideException;
import fr.epf.restaurant.model.CommandeClient;
import fr.epf.restaurant.model.LigneCommandeClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommandeClientService {

    private final CommandeClientDao commandeClientDao;
    private final ClientDao clientDao;
    private final PlatDao platDao;
    private final StockService stockService;

    public CommandeClientService(
            final CommandeClientDao commandeClientDao,
            final ClientDao clientDao,
            final PlatDao platDao,
            final StockService stockService
    ) {
        this.commandeClientDao = commandeClientDao;
        this.clientDao = clientDao;
        this.platDao = platDao;
        this.stockService = stockService;
    }

    /**
     * Retourne toutes les commandes client, avec filtre optionnel.
     */
    public List<CommandeClient> listerCommandes(final String statut) {
        if (statut == null || statut.isBlank()) {
            return commandeClientDao.findAll();
        }
        return commandeClientDao.findByStatut(statut);
    }

    /**
     * Retourne une commande client ou lève une exception si elle n'existe pas.
     */
    public CommandeClient getCommandeById(final Long id) {
        return commandeClientDao.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Commande client introuvable: " + id));
    }

    /**
     * Retourne les lignes d'une commande client.
     */
    public List<LigneCommandeClient> getLignesCommande(final Long idCommande) {
        return commandeClientDao.findLignesByCommandeId(idCommande);
    }

    @Transactional
    /**
     * Crée une commande client après validation du client et des plats.
     */
    public CommandeClient creerCommande(final CreerCommandeClientRequest request) {
        if (request == null) {
            throw new DonneesInvalidesException("La requete est vide");
        }
        if (request.getClientId() == null) {
            throw new DonneesInvalidesException("clientId obligatoire");
        }
        if (request.getLignes() == null || request.getLignes().isEmpty()) {
            throw new DonneesInvalidesException("Il faut au moins une ligne de commande");
        }

        clientDao.findById(request.getClientId())
                .orElseThrow(() -> new RessourceIntrouvableException(
                        "Client introuvable: " + request.getClientId()
                ));

        CommandeClient commande = commandeClientDao.save(request.getClientId());

        for (CreerCommandeClientRequest.LigneCommandeClientRequest ligne : request.getLignes()) {
            if (ligne.getPlatId() == null) {
                throw new DonneesInvalidesException("platId obligatoire");
            }
            if (ligne.getQuantite() == null || ligne.getQuantite() <= 0) {
                throw new DonneesInvalidesException("quantite doit etre > 0");
            }

            platDao.findById(ligne.getPlatId())
                    .orElseThrow(() -> new RessourceIntrouvableException("Plat introuvable: " + ligne.getPlatId()));

            commandeClientDao.addLigne(commande.getId(), ligne.getPlatId(), ligne.getQuantite());
        }

        return getCommandeById(commande.getId());
    }

    @Transactional
    /**
     * Passe une commande en préparation et déclenche la consommation du stock.
     */
    public PreparationResultDto preparerCommande(final Long commandeId) {
        CommandeClient commande = getCommandeById(commandeId);
        if (!"EN_ATTENTE".equals(commande.getStatut())) {
            throw new StatutInvalideException("La commande doit etre EN_ATTENTE pour etre preparee");
        }

        stockService.consommerStockPourCommandeClient(commandeId);
        commandeClientDao.updateStatut(commandeId, "EN_PREPARATION");

        PreparationResultDto result = new PreparationResultDto();
        result.setCommande(getCommandeById(commandeId));
        result.setAlertes(stockService.getAlertesStock());
        return result;
    }

    @Transactional
    /**
     * Passe une commande du statut EN_PREPARATION au statut SERVIE.
     */
    public CommandeClient servirCommande(final Long commandeId) {
        CommandeClient commande = getCommandeById(commandeId);
        if (!"EN_PREPARATION".equals(commande.getStatut())) {
            throw new StatutInvalideException("La commande doit etre EN_PREPARATION pour etre servie");
        }

        commandeClientDao.updateStatut(commandeId, "SERVIE");
        return getCommandeById(commandeId);
    }

    @Transactional
    /**
     * Supprime une commande et rend le stock si elle était en préparation.
     */
    public void supprimerCommande(final Long commandeId) {
        CommandeClient commande = getCommandeById(commandeId);
        if ("EN_PREPARATION".equals(commande.getStatut())) {
            stockService.restituerStockPourCommandeClient(commandeId);
        }
        commandeClientDao.deleteById(commandeId);
    }

    @Transactional
    /**
     * Annule une commande si son état le permet.
     */
    public CommandeClient annulerCommande(final Long commandeId) {
        CommandeClient commande = getCommandeById(commandeId);
        if ("SERVIE".equals(commande.getStatut())) {
            throw new StatutInvalideException("Une commande SERVIE ne peut pas etre annulee");
        }
        if ("ANNULEE".equals(commande.getStatut())) {
            throw new StatutInvalideException("La commande est deja annulee");
        }
        if ("EN_PREPARATION".equals(commande.getStatut())) {
            stockService.restituerStockPourCommandeClient(commandeId);
        }

        commandeClientDao.updateStatut(commandeId, "ANNULEE");
        return getCommandeById(commandeId);
    }
}
