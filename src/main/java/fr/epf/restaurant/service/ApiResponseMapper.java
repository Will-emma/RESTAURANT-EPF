package fr.epf.restaurant.service;

import fr.epf.restaurant.dao.ClientDao;
import fr.epf.restaurant.dao.CommandeClientDao;
import fr.epf.restaurant.dao.CommandeFournisseurDao;
import fr.epf.restaurant.dao.FournisseurDao;
import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dao.PlatDao;
import fr.epf.restaurant.dto.CommandeClientResponse;
import fr.epf.restaurant.dto.CommandeFournisseurResponse;
import fr.epf.restaurant.dto.LigneCommandeClientResponse;
import fr.epf.restaurant.dto.LigneCommandeFournisseurResponse;
import fr.epf.restaurant.dto.PlatDetailDto;
import fr.epf.restaurant.dto.PlatIngredientDto;
import fr.epf.restaurant.exception.RessourceIntrouvableException;
import fr.epf.restaurant.model.CommandeClient;
import fr.epf.restaurant.model.CommandeFournisseur;
import fr.epf.restaurant.model.Ingredient;
import fr.epf.restaurant.model.LigneCommandeClient;
import fr.epf.restaurant.model.LigneCommandeFournisseur;
import fr.epf.restaurant.model.Plat;
import fr.epf.restaurant.model.PlatIngredientBesoin;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiResponseMapper {

    private final ClientDao clientDao;
    private final PlatDao platDao;
    private final IngredientDao ingredientDao;
    private final FournisseurDao fournisseurDao;
    private final CommandeClientDao commandeClientDao;
    private final CommandeFournisseurDao commandeFournisseurDao;

    public ApiResponseMapper(
            final ClientDao clientDao,
            final PlatDao platDao,
            final IngredientDao ingredientDao,
            final FournisseurDao fournisseurDao,
            final CommandeClientDao commandeClientDao,
            final CommandeFournisseurDao commandeFournisseurDao
    ) {
        this.clientDao = clientDao;
        this.platDao = platDao;
        this.ingredientDao = ingredientDao;
        this.fournisseurDao = fournisseurDao;
        this.commandeClientDao = commandeClientDao;
        this.commandeFournisseurDao = commandeFournisseurDao;
    }

    /**
     * Transforme un plat simple en détail de plat avec ses ingrédients.
     */
    public PlatDetailDto toPlatDetail(final Plat plat) {
        PlatDetailDto dto = new PlatDetailDto();
        dto.setId(plat.getId());
        dto.setNom(plat.getNom());
        dto.setDescription(plat.getDescription());
        dto.setPrix(plat.getPrix());

        List<PlatIngredientDto> ingredients = new ArrayList<>();
        for (PlatIngredientBesoin besoin : platDao.findIngredientsByPlatId(plat.getId())) {
            Ingredient ingredient = ingredientDao.findById(besoin.getIngredientId())
                    .orElseThrow(() -> new RessourceIntrouvableException(
                            "Ingredient introuvable: " + besoin.getIngredientId()
                    ));

            PlatIngredientDto ingredientDto = new PlatIngredientDto();
            ingredientDto.setIngredient(ingredient);
            ingredientDto.setQuantiteRequise(besoin.getQuantiteRequise());
            ingredients.add(ingredientDto);
        }

        dto.setIngredients(ingredients);
        return dto;
    }

    /**
     * Transforme une commande client en réponse complète pour l'API.
     */
    public CommandeClientResponse toCommandeClientResponse(final CommandeClient commande) {
        CommandeClientResponse response = new CommandeClientResponse();
        response.setId(commande.getId());
        response.setClient(clientDao.findById(commande.getClientId())
                .orElseThrow(() -> new RessourceIntrouvableException(
                        "Client introuvable: " + commande.getClientId()
                )));
        response.setDateCommande(commande.getDateCommande());
        response.setStatut(commande.getStatut());

        List<LigneCommandeClientResponse> lignes = new ArrayList<>();
        // On enrichit les lignes avec le plat complet pour coller au JSON attendu par le frontend.
        for (LigneCommandeClient ligne : commandeClientDao.findLignesByCommandeId(commande.getId())) {
            LigneCommandeClientResponse ligneResponse = new LigneCommandeClientResponse();
            ligneResponse.setId(ligne.getId());
            ligneResponse.setCommandeClientId(ligne.getCommandeClientId());
            ligneResponse.setPlat(platDao.findById(ligne.getPlatId())
                    .orElseThrow(() -> new RessourceIntrouvableException("Plat introuvable: " + ligne.getPlatId())));
            ligneResponse.setQuantite(ligne.getQuantite());
            lignes.add(ligneResponse);
        }

        response.setLignes(lignes);
        return response;
    }

    /**
     * Transforme une commande fournisseur en réponse complète pour l'API.
     */
    public CommandeFournisseurResponse toCommandeFournisseurResponse(final CommandeFournisseur commande) {
        CommandeFournisseurResponse response = new CommandeFournisseurResponse();
        response.setId(commande.getId());
        response.setFournisseur(fournisseurDao.findById(commande.getFournisseurId())
                .orElseThrow(() -> new RessourceIntrouvableException(
                        "Fournisseur introuvable: " + commande.getFournisseurId()
                )));
        response.setDateCommande(commande.getDateCommande());
        response.setStatut(commande.getStatut());

        List<LigneCommandeFournisseurResponse> lignes = new ArrayList<>();
        // Même idée ici : chaque ligne contient directement l'ingrédient complet.
        for (LigneCommandeFournisseur ligne : commandeFournisseurDao.findLignesByCommandeId(commande.getId())) {
            LigneCommandeFournisseurResponse ligneResponse = new LigneCommandeFournisseurResponse();
            ligneResponse.setId(ligne.getId());
            ligneResponse.setCommandeFournisseurId(ligne.getCommandeFournisseurId());
            ligneResponse.setIngredient(ingredientDao.findById(ligne.getIngredientId())
                    .orElseThrow(() -> new RessourceIntrouvableException(
                            "Ingredient introuvable: " + ligne.getIngredientId()
                    )));
            ligneResponse.setQuantiteCommandee(ligne.getQuantiteCommandee());
            ligneResponse.setPrixUnitaire(ligne.getPrixUnitaire());
            lignes.add(ligneResponse);
        }

        response.setLignes(lignes);
        return response;
    }
}
