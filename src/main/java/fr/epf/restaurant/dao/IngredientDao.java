package fr.epf.restaurant.dao;

import fr.epf.restaurant.model.Ingredient;
import fr.epf.restaurant.model.PrixIngredientItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class IngredientDao {

    private final JdbcTemplate jdbcTemplate;

    public IngredientDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retourne tous les ingrédients.
     */
    public List<Ingredient> findAll() {
        String sql = "SELECT id, nom, unite, stock_actuel, seuil_alerte FROM INGREDIENT ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(rs.getLong("id"));
            ingredient.setNom(rs.getString("nom"));
            ingredient.setUnite(rs.getString("unite"));
            ingredient.setStockActuel(rs.getDouble("stock_actuel"));
            ingredient.setSeuilAlerte(rs.getDouble("seuil_alerte"));
            return ingredient;
        });
    }

    /**
     * Cherche un ingrédient par son identifiant.
     */
    public Optional<Ingredient> findById(final Long id) {
        String sql = "SELECT id, nom, unite, stock_actuel, seuil_alerte FROM INGREDIENT WHERE id = ?";
        List<Ingredient> ingredients = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(rs.getLong("id"));
            ingredient.setNom(rs.getString("nom"));
            ingredient.setUnite(rs.getString("unite"));
            ingredient.setStockActuel(rs.getDouble("stock_actuel"));
            ingredient.setSeuilAlerte(rs.getDouble("seuil_alerte"));
            return ingredient;
        }, id);
        if (ingredients.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ingredients.get(0));
    }

    /**
     * Insère un ingrédient et récupère son identifiant.
     */
    public Ingredient save(final Ingredient ingredient) {
        String sql = "INSERT INTO INGREDIENT (nom, unite, stock_actuel, seuil_alerte) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ingredient.getNom());
            ps.setString(2, ingredient.getUnite());
            ps.setDouble(3, ingredient.getStockActuel());
            ps.setDouble(4, ingredient.getSeuilAlerte());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) {
            ingredient.setId(key.longValue());
        }
        return ingredient;
    }

    /**
     * Ajoute une quantité au stock d'un ingrédient.
     */
    public void incrementStock(final Long ingredientId, final Double quantite) {
        String sql = "UPDATE INGREDIENT SET stock_actuel = stock_actuel + ? WHERE id = ?";
        jdbcTemplate.update(sql, quantite, ingredientId);
    }

    /**
     * Retire une quantité du stock d'un ingrédient.
     */
    public void decrementStock(final Long ingredientId, final Double quantite) {
        String sql = "UPDATE INGREDIENT SET stock_actuel = stock_actuel - ? WHERE id = ?";
        jdbcTemplate.update(sql, quantite, ingredientId);
    }

    /**
     * Retourne les ingrédients dont le stock est strictement sous le seuil.
     */
    public List<Ingredient> findIngredientsSousAlerte() {
        String sql = """
                SELECT id, nom, unite, stock_actuel, seuil_alerte
                FROM INGREDIENT
                WHERE stock_actuel < seuil_alerte
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(rs.getLong("id"));
            ingredient.setNom(rs.getString("nom"));
            ingredient.setUnite(rs.getString("unite"));
            ingredient.setStockActuel(rs.getDouble("stock_actuel"));
            ingredient.setSeuilAlerte(rs.getDouble("seuil_alerte"));
            return ingredient;
        });
    }

    /**
     * Retourne les prix proposés pour un ingrédient.
     */
    public List<PrixIngredientItem> findPrixParIngredient(final Long ingredientId) {
        String sql = """
                SELECT f.id AS fournisseur_id, f.nom AS fournisseur_nom, fi.prix_unitaire AS prix_unitaire
                FROM FOURNISSEUR_INGREDIENT fi
                JOIN FOURNISSEUR f ON f.id = fi.fournisseur_id
                WHERE fi.ingredient_id = ?
                ORDER BY fi.prix_unitaire ASC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            PrixIngredientItem item = new PrixIngredientItem();
            item.setFournisseurId(rs.getLong("fournisseur_id"));
            item.setFournisseurNom(rs.getString("fournisseur_nom"));
            item.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
            return item;
        }, ingredientId);
    }
}
