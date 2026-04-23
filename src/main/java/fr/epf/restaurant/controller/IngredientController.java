package fr.epf.restaurant.controller;

import fr.epf.restaurant.dao.IngredientDao;
import fr.epf.restaurant.dto.RecommandationDto;
import fr.epf.restaurant.exception.RessourceIntrouvableException;
import fr.epf.restaurant.model.Ingredient;
import fr.epf.restaurant.model.PrixIngredientItem;
import fr.epf.restaurant.service.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientDao ingredientDao;
    private final StockService stockService;

    public IngredientController(final IngredientDao ingredientDao, final StockService stockService) {
        this.ingredientDao = ingredientDao;
        this.stockService = stockService;
    }

    /**
     * Retourne tous les ingrédients.
     */
    @GetMapping
    public List<Ingredient> getAll() {
        return ingredientDao.findAll();
    }

    /**
     * Retourne les ingrédients dont le stock est sous le seuil.
     */
    @GetMapping("/alertes")
    public List<Ingredient> getAlertes() {
        return ingredientDao.findIngredientsSousAlerte();
    }

    /**
     * Retourne les prix proposés par les fournisseurs pour un ingrédient.
     */
    @GetMapping("/{id}/prix")
    public List<PrixIngredientItem> getPrix(@PathVariable final Long id) {
        ingredientDao.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Ingredient introuvable: " + id));
        return ingredientDao.findPrixParIngredient(id);
    }

    /**
     * Retourne la recommandation d'achat pour un ingrédient.
     */
    @GetMapping("/{id}/recommandation")
    public RecommandationDto getRecommandation(@PathVariable final Long id) {
        return stockService.getRecommandationPourIngredient(id);
    }
}
