package fr.epf.restaurant.dao;

import fr.epf.restaurant.model.PlatIngredientBesoin;
import fr.epf.restaurant.model.Plat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class PlatDao {

    private final JdbcTemplate jdbcTemplate;

    public PlatDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retourne tous les plats.
     */
    public List<Plat> findAll() {
        String sql = "SELECT id, nom, description, prix FROM PLAT ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Plat plat = new Plat();
            plat.setId(rs.getLong("id"));
            plat.setNom(rs.getString("nom"));
            plat.setDescription(rs.getString("description"));
            plat.setPrix(rs.getBigDecimal("prix"));
            return plat;
        });
    }

    /**
     * Cherche un plat par son identifiant.
     */
    public Optional<Plat> findById(final Long id) {
        String sql = "SELECT id, nom, description, prix FROM PLAT WHERE id = ?";
        List<Plat> plats = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Plat plat = new Plat();
            plat.setId(rs.getLong("id"));
            plat.setNom(rs.getString("nom"));
            plat.setDescription(rs.getString("description"));
            plat.setPrix(rs.getBigDecimal("prix"));
            return plat;
        }, id);
        if (plats.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(plats.get(0));
    }

    /**
     * Insère un plat et récupère son identifiant.
     */
    public Plat save(final Plat plat) {
        String sql = "INSERT INTO PLAT (nom, description, prix) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, plat.getNom());
            ps.setString(2, plat.getDescription());
            ps.setBigDecimal(3, plat.getPrix());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) {
            plat.setId(key.longValue());
        }
        return plat;
    }

    /**
     * Retourne les besoins en ingrédients d'un plat.
     */
    public List<PlatIngredientBesoin> findIngredientsByPlatId(final Long platId) {
        String sql = """
                SELECT i.id AS ingredient_id, i.nom AS ingredient_nom, i.unite, i.stock_actuel, i.seuil_alerte,
                       pi.quantite_requise
                FROM PLAT_INGREDIENT pi
                JOIN INGREDIENT i ON i.id = pi.ingredient_id
                WHERE pi.plat_id = ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            PlatIngredientBesoin besoin = new PlatIngredientBesoin();
            besoin.setIngredientId(rs.getLong("ingredient_id"));
            besoin.setIngredientNom(rs.getString("ingredient_nom"));
            besoin.setUnite(rs.getString("unite"));
            besoin.setStockActuel(rs.getDouble("stock_actuel"));
            besoin.setSeuilAlerte(rs.getDouble("seuil_alerte"));
            besoin.setQuantiteRequise(rs.getDouble("quantite_requise"));
            return besoin;
        }, platId);
    }
}
