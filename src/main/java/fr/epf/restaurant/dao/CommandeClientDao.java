package fr.epf.restaurant.dao;

import fr.epf.restaurant.model.CommandeClient;
import fr.epf.restaurant.model.LigneCommandeClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class CommandeClientDao {

    private final JdbcTemplate jdbcTemplate;

    public CommandeClientDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retourne toutes les commandes client.
     */
    public List<CommandeClient> findAll() {
        String sql = "SELECT id, client_id, date_commande, statut FROM COMMANDE_CLIENT ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CommandeClient commande = new CommandeClient();
            commande.setId(rs.getLong("id"));
            commande.setClientId(rs.getLong("client_id"));
            commande.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
            commande.setStatut(rs.getString("statut"));
            return commande;
        });
    }

    /**
     * Retourne les commandes client d'un statut donné.
     */
    public List<CommandeClient> findByStatut(final String statut) {
        String sql = "SELECT id, client_id, date_commande, statut FROM COMMANDE_CLIENT WHERE statut = ? ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CommandeClient commande = new CommandeClient();
            commande.setId(rs.getLong("id"));
            commande.setClientId(rs.getLong("client_id"));
            commande.setDateCommande(rs.getTimestamp("date_commande").toLocalDateTime());
            commande.setStatut(rs.getString("statut"));
            return commande;
        }, statut);
    }

    /**
     * Cherche une commande client par son identifiant.
     */
    public Optional<CommandeClient> findById(final Long id) {
        String sql = "SELECT id, client_id, date_commande, statut FROM COMMANDE_CLIENT WHERE id = ?";
        List<CommandeClient> commandes = jdbcTemplate.query(sql, (rs, rowNum) -> {
            CommandeClient commande = new CommandeClient();
            commande.setId(rs.getLong("id"));
            commande.setClientId(rs.getLong("client_id"));
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
     * Crée une commande client avec le statut initial EN_ATTENTE.
     */
    public CommandeClient save(final Long clientId) {
        String sql = "INSERT INTO COMMANDE_CLIENT (client_id, date_commande, statut) VALUES (?, ?, ?)";
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String statut = "EN_ATTENTE";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            // On demande explicitement la colonne ID pour éviter que H2 renvoie plusieurs clés.
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, clientId);
            ps.setTimestamp(2, now);
            ps.setString(3, statut);
            return ps;
        }, keyHolder);

        CommandeClient commande = new CommandeClient();
        Number key = keyHolder.getKey();
        if (key != null) {
            commande.setId(key.longValue());
        }
        commande.setClientId(clientId);
        commande.setDateCommande(now.toLocalDateTime());
        commande.setStatut(statut);
        return commande;
    }

    /**
     * Ajoute une ligne à une commande client.
     */
    public void addLigne(final Long commandeClientId, final Long platId, final Integer quantite) {
        String sql = "INSERT INTO LIGNE_COMMANDE_CLIENT (commande_client_id, plat_id, quantite) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, commandeClientId, platId, quantite);
    }

    /**
     * Retourne les lignes d'une commande client.
     */
    public List<LigneCommandeClient> findLignesByCommandeId(final Long commandeClientId) {
        String sql = """
                SELECT id, commande_client_id, plat_id, quantite
                FROM LIGNE_COMMANDE_CLIENT
                WHERE commande_client_id = ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            LigneCommandeClient ligne = new LigneCommandeClient();
            ligne.setId(rs.getLong("id"));
            ligne.setCommandeClientId(rs.getLong("commande_client_id"));
            ligne.setPlatId(rs.getLong("plat_id"));
            ligne.setQuantite(rs.getInt("quantite"));
            return ligne;
        }, commandeClientId);
    }

    /**
     * Modifie le statut d'une commande client.
     */
    public void updateStatut(final Long commandeId, final String statut) {
        String sql = "UPDATE COMMANDE_CLIENT SET statut = ? WHERE id = ?";
        jdbcTemplate.update(sql, statut, commandeId);
    }

    /**
     * Supprime une commande client et ses lignes.
     */
    public void deleteById(final Long commandeId) {
        String deleteLignes = "DELETE FROM LIGNE_COMMANDE_CLIENT WHERE commande_client_id = ?";
        String deleteCommande = "DELETE FROM COMMANDE_CLIENT WHERE id = ?";
        jdbcTemplate.update(deleteLignes, commandeId);
        jdbcTemplate.update(deleteCommande, commandeId);
    }
}
