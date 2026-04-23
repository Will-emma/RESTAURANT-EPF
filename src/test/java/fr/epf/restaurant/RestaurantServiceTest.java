package fr.epf.restaurant;

import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dto.CreerCommandeClientRequest;
import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest;
import fr.epf.restaurant.dto.RecommandationDto;
import fr.epf.restaurant.exception.RessourceIntrouvableException;
import fr.epf.restaurant.exception.StockInsuffisantException;
import fr.epf.restaurant.exception.StatutInvalideException;
import fr.epf.restaurant.model.CommandeClient;
import fr.epf.restaurant.model.CommandeFournisseur;
import fr.epf.restaurant.model.Ingredient;
import fr.epf.restaurant.service.CommandeClientService;
import fr.epf.restaurant.service.CommandeFournisseurService;
import fr.epf.restaurant.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestaurantServiceTest {

    @Autowired
    private CommandeClientService commandeClientService;

    @Autowired
    private CommandeFournisseurService commandeFournisseurService;

    @Autowired
    private StockService stockService;

    @Autowired
    private IngredientDao ingredientDao;

    /**
     * Vérifie qu'une commande client valide est bien créée.
     */
    @Test
    void creerCommandeClientValide() {
        // Cas simple : un client existant commande un plat existant.
        CreerCommandeClientRequest request = creerCommandeClientRequest(1L, 1L, 1);

        CommandeClient commande = commandeClientService.creerCommande(request);

        assertNotNull(commande.getId());
        assertEquals("EN_ATTENTE", commande.getStatut());
    }

    /**
     * Vérifie qu'on refuse la création si le client n'existe pas.
     */
    @Test
    void creerCommandeClientAvecClientInexistant() {
        // On vérifie qu'on refuse une commande si le client n'existe pas.
        CreerCommandeClientRequest request = creerCommandeClientRequest(999L, 1L, 1);

        assertThrows(RessourceIntrouvableException.class, () -> commandeClientService.creerCommande(request));
    }

    /**
     * Vérifie que préparer une commande diminue bien le stock.
     */
    @Test
    void preparerCommandeConsommeLeStock() {
        // Une quiche consomme 200g de pâte brisée.
        Ingredient avant = getIngredient(1L);
        CommandeClient commande = commandeClientService.creerCommande(creerCommandeClientRequest(1L, 1L, 1));

        commandeClientService.preparerCommande(commande.getId());

        Ingredient apres = getIngredient(1L);
        assertEquals(avant.getStockActuel() - 200.0, apres.getStockActuel(), 0.001);
        assertEquals("EN_PREPARATION", commandeClientService.getCommandeById(commande.getId()).getStatut());
    }

    /**
     * Vérifie qu'une commande est bloquée si le stock est insuffisant.
     */
    @Test
    void preparerCommandeAvecStockInsuffisant() {
        // 3 quiches demandent 600g de pâte brisée alors que le stock initial est 500g.
        CommandeClient commande = commandeClientService.creerCommande(creerCommandeClientRequest(1L, 1L, 3));

        assertThrows(StockInsuffisantException.class, () -> commandeClientService.preparerCommande(commande.getId()));
    }

    /**
     * Vérifie qu'une commande préparée peut être servie.
     */
    @Test
    void servirCommandePasseLeStatutAServie() {
        CommandeClient commande = commandeClientService.creerCommande(creerCommandeClientRequest(1L, 1L, 1));
        commandeClientService.preparerCommande(commande.getId());

        CommandeClient commandeServie = commandeClientService.servirCommande(commande.getId());

        assertEquals("SERVIE", commandeServie.getStatut());
    }

    /**
     * Vérifie que l'annulation rend le stock si la préparation a déjà eu lieu.
     */
    @Test
    void annulerCommandeEnPreparationRendLeStock() {
        // Si on annule après préparation, le stock doit revenir à sa valeur précédente.
        Ingredient avant = getIngredient(1L);
        CommandeClient commande = commandeClientService.creerCommande(creerCommandeClientRequest(1L, 1L, 1));
        commandeClientService.preparerCommande(commande.getId());

        CommandeClient annulee = commandeClientService.annulerCommande(commande.getId());

        Ingredient apres = getIngredient(1L);
        assertEquals("ANNULEE", annulee.getStatut());
        assertEquals(avant.getStockActuel(), apres.getStockActuel(), 0.001);
    }

    /**
     * Vérifie qu'un ingrédient sous le seuil apparaît dans les alertes.
     */
    @Test
    void ingredientSousLeSeuilEstRetourneDansLesAlertes() {
        // On force un stock sous le seuil pour vérifier la règle d'alerte.
        ingredientDao.decrementStock(1L, 350.0);

        assertTrue(stockService.getAlertesStock().stream().anyMatch(a -> a.getIngredientId().equals(1L)));
    }

    /**
     * Vérifie le fournisseur recommandé et la quantité conseillée.
     */
    @Test
    void recommandationRetourneLeMoinsCherEtLaBonneQuantite() {
        // On baisse le stock de l'ingrédient 1 de 500 à 100 pour déclencher la formule 2 x (seuil - stock).
        ingredientDao.decrementStock(1L, 400.0);

        RecommandationDto recommandation = stockService.getRecommandationPourIngredient(1L);

        assertEquals(2L, recommandation.getFournisseurId());
        assertEquals(0, BigDecimal.valueOf(0.05).compareTo(recommandation.getPrixUnitaire()));
        assertEquals(200.0, recommandation.getQuantiteRecommandee(), 0.001);
    }

    /**
     * Vérifie que la réception d'une commande fournisseur augmente le stock.
     */
    @Test
    void recevoirCommandeFournisseurMetAJourLeStock() {
        // Après réception, le stock de l'ingrédient commandé doit augmenter.
        Ingredient avant = getIngredient(1L);
        CommandeFournisseur commande = commandeFournisseurService.creerCommande(
                creerCommandeFournisseurRequest(1L, 1L, 50.0, BigDecimal.valueOf(0.06))
        );
        commandeFournisseurService.envoyerCommande(commande.getId());

        CommandeFournisseur recue = commandeFournisseurService.recevoirCommande(commande.getId());

        Ingredient apres = getIngredient(1L);
        assertEquals("RECUE", recue.getStatut());
        assertEquals(avant.getStockActuel() + 50.0, apres.getStockActuel(), 0.001);
    }

    /**
     * Vérifie qu'on ne peut pas recevoir une commande non envoyée.
     */
    @Test
    void recevoirCommandeFournisseurSansEnvoiAvant() {
        // La transition EN_ATTENTE -> RECUE n'est pas autorisée.
        CommandeFournisseur commande = commandeFournisseurService.creerCommande(
                creerCommandeFournisseurRequest(1L, 1L, 10.0, BigDecimal.valueOf(0.06))
        );

        assertThrows(
                StatutInvalideException.class,
                () -> commandeFournisseurService.recevoirCommande(commande.getId())
        );
    }

    /**
     * Prépare rapidement une requête de création de commande client pour les tests.
     */
    private CreerCommandeClientRequest creerCommandeClientRequest(
            final Long clientId,
            final Long platId,
            final Integer quantite
    ) {
        CreerCommandeClientRequest request = new CreerCommandeClientRequest();
        request.setClientId(clientId);

        CreerCommandeClientRequest.LigneCommandeClientRequest ligne =
                new CreerCommandeClientRequest.LigneCommandeClientRequest();
        ligne.setPlatId(platId);
        ligne.setQuantite(quantite);

        request.getLignes().add(ligne);
        return request;
    }

    /**
     * Prépare rapidement une requête de création de commande fournisseur pour les tests.
     */
    private CreerCommandeFournisseurRequest creerCommandeFournisseurRequest(
            final Long fournisseurId,
            final Long ingredientId,
            final Double quantite,
            final BigDecimal prixUnitaire
    ) {
        CreerCommandeFournisseurRequest request = new CreerCommandeFournisseurRequest();
        request.setFournisseurId(fournisseurId);

        CreerCommandeFournisseurRequest.LigneCommandeFournisseurRequest ligne =
                new CreerCommandeFournisseurRequest.LigneCommandeFournisseurRequest();
        ligne.setIngredientId(ingredientId);
        ligne.setQuantite(quantite);
        ligne.setPrixUnitaire(prixUnitaire);

        request.getLignes().add(ligne);
        return request;
    }

    /**
     * Recharge un ingrédient depuis la base pendant les tests.
     */
    private Ingredient getIngredient(final Long id) {
        return ingredientDao.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Ingredient introuvable: " + id));
    }
}
