package fr.epf.restaurant.controller;

import fr.epf.restaurant.dto.CommandeFournisseurResponse;
import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest;
import fr.epf.restaurant.service.ApiResponseMapper;
import fr.epf.restaurant.service.CommandeFournisseurService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/commandes/fournisseur")
public class CommandeFournisseurController {

    private final CommandeFournisseurService commandeFournisseurService;
    private final ApiResponseMapper apiResponseMapper;

    public CommandeFournisseurController(
            final CommandeFournisseurService commandeFournisseurService,
            final ApiResponseMapper apiResponseMapper
    ) {
        this.commandeFournisseurService = commandeFournisseurService;
        this.apiResponseMapper = apiResponseMapper;
    }

    /**
     * Retourne les commandes fournisseur, avec filtre optionnel par statut.
     */
    @GetMapping
    public List<CommandeFournisseurResponse> getAll(@RequestParam(required = false) final String statut) {
        List<CommandeFournisseurResponse> result = new ArrayList<>();
        commandeFournisseurService.listerCommandes(statut)
                .forEach(commande -> result.add(apiResponseMapper.toCommandeFournisseurResponse(commande)));
        return result;
    }

    /**
     * Retourne une commande fournisseur.
     */
    @GetMapping("/{id}")
    public CommandeFournisseurResponse getById(@PathVariable final Long id) {
        return apiResponseMapper.toCommandeFournisseurResponse(commandeFournisseurService.getCommandeById(id));
    }

    /**
     * Crée une commande fournisseur.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommandeFournisseurResponse create(@RequestBody final CreerCommandeFournisseurRequest request) {
        return apiResponseMapper.toCommandeFournisseurResponse(
                commandeFournisseurService.creerCommande(request)
        );
    }

    /**
     * Passe une commande fournisseur au statut ENVOYEE.
     */
    @PutMapping("/{id}/envoyer")
    public CommandeFournisseurResponse envoyer(@PathVariable final Long id) {
        return apiResponseMapper.toCommandeFournisseurResponse(commandeFournisseurService.envoyerCommande(id));
    }

    /**
     * Réceptionne une commande fournisseur et met à jour le stock.
     */
    @PutMapping("/{id}/recevoir")
    public CommandeFournisseurResponse recevoir(@PathVariable final Long id) {
        return apiResponseMapper.toCommandeFournisseurResponse(commandeFournisseurService.recevoirCommande(id));
    }

    /**
     * Annule une commande fournisseur.
     */
    @PutMapping("/{id}/annuler")
    public CommandeFournisseurResponse annuler(@PathVariable final Long id) {
        return apiResponseMapper.toCommandeFournisseurResponse(commandeFournisseurService.annulerCommande(id));
    }

    /**
     * Supprime définitivement une commande fournisseur.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        commandeFournisseurService.supprimerCommande(id);
    }
}
