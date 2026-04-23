package fr.epf.restaurant.controller;

import fr.epf.restaurant.dao.FournisseurDao;
import fr.epf.restaurant.exception.RessourceIntrouvableException;
import fr.epf.restaurant.model.CatalogueFournisseurItem;
import fr.epf.restaurant.model.Fournisseur;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurController {

    private final FournisseurDao fournisseurDao;

    public FournisseurController(final FournisseurDao fournisseurDao) {
        this.fournisseurDao = fournisseurDao;
    }

    /**
     * Retourne la liste des fournisseurs.
     */
    @GetMapping
    public List<Fournisseur> getAll() {
        return fournisseurDao.findAll();
    }

    /**
     * Crée un fournisseur.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fournisseur create(@RequestBody final Fournisseur fournisseur) {
        return fournisseurDao.save(fournisseur);
    }

    /**
     * Retourne le catalogue de prix d'un fournisseur.
     */
    @GetMapping("/{id}/catalogue")
    public List<CatalogueFournisseurItem> getCatalogue(@PathVariable final Long id) {
        fournisseurDao.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Fournisseur introuvable: " + id));
        return fournisseurDao.findCatalogueByFournisseurId(id);
    }
}
