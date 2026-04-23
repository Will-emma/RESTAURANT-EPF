package fr.epf.restaurant.controller;

import fr.epf.restaurant.dto.CommandeClientResponse;
import fr.epf.restaurant.dto.CreerCommandeClientRequest;
import fr.epf.restaurant.dto.PreparationResultDto;
import fr.epf.restaurant.service.ApiResponseMapper;
import fr.epf.restaurant.service.CommandeClientService;
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
@RequestMapping("/api/commandes/client")
public class CommandeClientController {

    private final CommandeClientService commandeClientService;
    private final ApiResponseMapper apiResponseMapper;

    public CommandeClientController(
            final CommandeClientService commandeClientService,
            final ApiResponseMapper apiResponseMapper
    ) {
        this.commandeClientService = commandeClientService;
        this.apiResponseMapper = apiResponseMapper;
    }

    /**
     * Retourne les commandes client, avec filtre optionnel par statut.
     */
    @GetMapping
    public List<CommandeClientResponse> getAll(@RequestParam(required = false) final String statut) {
        List<CommandeClientResponse> result = new ArrayList<>();
        commandeClientService.listerCommandes(statut)
                .forEach(commande -> result.add(apiResponseMapper.toCommandeClientResponse(commande)));
        return result;
    }

    /**
     * Retourne une commande client par son identifiant.
     */
    @GetMapping("/{id}")
    public CommandeClientResponse getById(@PathVariable final Long id) {
        return apiResponseMapper.toCommandeClientResponse(commandeClientService.getCommandeById(id));
    }

    /**
     * Crée une commande client.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommandeClientResponse create(@RequestBody final CreerCommandeClientRequest request) {
        return apiResponseMapper.toCommandeClientResponse(commandeClientService.creerCommande(request));
    }

    /**
     * Passe une commande client en préparation.
     */
    @PutMapping("/{id}/preparer")
    public PreparationResultDto preparer(@PathVariable final Long id) {
        PreparationResultDto result = commandeClientService.preparerCommande(id);
        result.setCommande(apiResponseMapper.toCommandeClientResponse(commandeClientService.getCommandeById(id)));
        return result;
    }

    /**
     * Marque une commande client comme servie.
     */
    @PutMapping("/{id}/servir")
    public CommandeClientResponse servir(@PathVariable final Long id) {
        return apiResponseMapper.toCommandeClientResponse(commandeClientService.servirCommande(id));
    }

    /**
     * Annule une commande client.
     */
    @PutMapping("/{id}/annuler")
    public CommandeClientResponse annuler(@PathVariable final Long id) {
        return apiResponseMapper.toCommandeClientResponse(commandeClientService.annulerCommande(id));
    }

    /**
     * Supprime définitivement une commande client.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        commandeClientService.supprimerCommande(id);
    }
}
