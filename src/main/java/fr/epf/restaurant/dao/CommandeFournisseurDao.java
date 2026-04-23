package fr.epf.restaurant.dao;

import fr.epf.restaurant.model.CommandeFournisseur;
import fr.epf.restaurant.model.LigneCommandeFournisseur;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class CommandeFournisseurDao {

    private final JdbcTemplate jdbcTemplate;

    public CommandeFournisseurDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retourne toutes les commandes fournisseur.
     */
    public List<CommandeFournisseur> findAll() {
        String sql = "SELECT id, fournisseur_id, date_commande, statut FROM COMMANDE_FOURNISSEUR ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CommandeFournisseur commande = new CommandeFournisseur();
            commande.setId(rs.getLong("id"));
            commande.setFournisseurId(rs.getLong("fournisseur_id"));
            commande.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
            commande.setStatut(rs.getString("statut"));
            return commande;
        });
    }

    /**
     * Retourne les commandes fournisseur d'un statut donné.
     */
    public List<CommandeFournisseur> findByStatut(final String statut) {
        String sql = "SELECT id, fournisseur_id, date_commande, statut FROM COMMANDE_FOURNISSEUR WHERE statut = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CommandeFournisseur commande = new CommandeFournisseur();
            commande.setId(rs.getLong("id"));
            commande.setFournisseurId(rs.getLong("fournisseur_id"));
            commande.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
            commande.setStatut(rs.getString("statut"));
            return commande;
        }, statut);
    }

    /**
     * Cherche une commande fournisseur par son identifiant.
     */
    public Optional<CommandeFournisseur> findById(final Long id) {
        String sql = "SELECT id, fournisseur_id, date_commande, statut FROM COMMANDE_FOURNISSEUR WHERE id = ?";
        List<CommandeFournisseur> commandes = jdbcTemplate.query(sql, (rs, rowNum) -> {
            CommandeFournisseur commande = new CommandeFournisseur();
            commande.setId(rs.getLong("id"));
            commande.setFournisseurId(rs.getLong("fournisseur_id"));
            commande.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
            commande.setStatut(rs.getString("statut"));
            return commande;
        }, id);
        if (commandes.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(commandes.get(0));
    }

    /**
     * Crée une commande fournisseur avec le statut initial EN_ATTENTE.
     */
    public CommandeFournisseur save(final Long fournisseurId) {
        String sql = "INSERT INTO COMMANDE_FOURNISSEUR (fournisseur_id, date_commande, statut) VALUES (?, ?, ?)";
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String statut = "EN_ATTENTE";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            // Même chose ici : on ne récupère que l'identifiant généré.
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, fournisseurId);
            ps.setTimestamp(2, now);
            ps.setString(3, statut);
            return ps;
        }, keyHolder);

        CommandeFournisseur commande = new CommandeFournisseur();
        Number key = keyHolder.getKey();
        if (key != null) {
            commande.setId(key.longValue());
        }
        commande.setFournisseurId(fournisseurId);
        commande.setDateCommande(now.toLocalDateTime());
        commande.setStatut(statut);
        return commande;
    }

    /**
     * Ajoute une ligne à une commande fournisseur.
     */
    public void addLigne(
            final Long commandeFournisseurId,
            final Long ingredientId,
            final Double quantiteCommandee,
            final BigDecimal prixUnitaire
    ) {
        String sql = """
                INSERT INTO LIGNE_COMMANDE_FOURNISSEUR
                (commande_fournisseur_id, ingredient_id, quantite_commandee, prix_unitaire)
                VALUES (?, ?, ?, ?)
                """;
        jdbcTemplate.update(sql, commandeFournisseurId, ingredientId, quantiteCommandee, prixUnitaire);
    }

    /**
     * Retourne les lignes d'une commande fournisseur.
     */
    public List<LigneCommandeFournisseur> findLignesByCommandeId(final Long commandeFournisseurId) {
        String sql = """
                SELECT id, commande_fournisseur_id, ingredient_id, quantite_commandee, prix_unitaire
                FROM LIGNE_COMMANDE_FOURNISSEUR
                WHERE commande_fournisseur_id = ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LigneCommandeFournisseur ligne = new LigneCommandeFournisseur();
            ligne.setId(rs.getLong("id"));
            ligne.setCommandeFournisseurId(rs.getLong("commande_fournisseur_id"));
            ligne.setIngredientId(rs.getLong("ingredient_id"));
            ligne.setQuantiteCommandee(rs.getDouble("quantite_commandee"));
            ligne.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
            return ligne;
        }, commandeFournisseurId);
    }

    /**
     * Modifie le statut d'une commande fournisseur.
     */
    public void updateStatut(final Long commandeId, final String statut) {
        String sql = "UPDATE COMMANDE_FOURNISSEUR SET statut = ? WHERE id = ?";
        jdbcTemplate.update(sql, statut, commandeId);
    }

    /**
     * Supprime une commande fournisseur et ses lignes.
     */
    public void deleteById(final Long commandeId) {
        String deleteLignes = "DELETE FROM LIGNE_COMMANDE_FOURNISSEUR WHERE commande_fournisseur_id = ?";
        String deleteCommande = "DELETE FROM COMMANDE_FOURNISSEUR WHERE id = ?";
        jdbcTemplate.update(deleteLignes, commandeId);
        jdbcTemplate.update(deleteCommande, commandeId);
    }
}
