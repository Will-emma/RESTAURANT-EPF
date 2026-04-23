package fr.epf.restaurant.dao;

import fr.epf.restaurant.model.CatalogueFournisseurItem;
import fr.epf.restaurant.model.Fournisseur;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class FournisseurDao {

    private final JdbcTemplate jdbcTemplate;

    public FournisseurDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retourne tous les fournisseurs.
     */
    public List<Fournisseur> findAll() {
        String sql = "SELECT id, nom, contact, email FROM FOURNISSEUR ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Fournisseur fournisseur = new Fournisseur();
            fournisseur.setId(rs.getLong("id"));
            fournisseur.setNom(rs.getString("nom"));
            fournisseur.setContact(rs.getString("contact"));
            fournisseur.setEmail(rs.getString("email"));
            return fournisseur;
        });
    }

    /**
     * Cherche un fournisseur par son identifiant.
     */
    public Optional<Fournisseur> findById(final Long id) {
        String sql = "SELECT id, nom, contact, email FROM FOURNISSEUR WHERE id = ?";
        List<Fournisseur> fournisseurs = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Fournisseur fournisseur = new Fournisseur();
            fournisseur.setId(rs.getLong("id"));
            fournisseur.setNom(rs.getString("nom"));
            fournisseur.setContact(rs.getString("contact"));
            fournisseur.setEmail(rs.getString("email"));
            return fournisseur;
        }, id);
        if (fournisseurs.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(fournisseurs.get(0));
    }

    /**
     * Insère un fournisseur et récupère son identifiant.
     */
    public Fournisseur save(final Fournisseur fournisseur) {
        String sql = "INSERT INTO FOURNISSEUR (nom, contact, email) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, fournisseur.getNom());
            ps.setString(2, fournisseur.getContact());
            ps.setString(3, fournisseur.getEmail());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) {
            fournisseur.setId(key.longValue());
        }
        return fournisseur;
    }

    /**
     * Retourne le catalogue de prix d'un fournisseur.
     */
    public List<CatalogueFournisseurItem> findCatalogueByFournisseurId(final Long fournisseurId) {
        String sql = """
                SELECT i.id AS ingredient_id, i.nom AS ingredient_nom, i.unite AS ingredient_unite,
                       fi.prix_unitaire AS prix_unitaire
                FROM FOURNISSEUR_INGREDIENT fi
                JOIN INGREDIENT i ON i.id = fi.ingredient_id
                WHERE fi.fournisseur_id = ?
                ORDER BY i.nom
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CatalogueFournisseurItem item = new CatalogueFournisseurItem();
            item.setIngredientId(rs.getLong("ingredient_id"));
            item.setIngredientNom(rs.getString("ingredient_nom"));
            item.setIngredientUnite(rs.getString("ingredient_unite"));
            item.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
            return item;
        }, fournisseurId);
    }
}
