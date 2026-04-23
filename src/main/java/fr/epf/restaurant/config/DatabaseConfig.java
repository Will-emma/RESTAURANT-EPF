package fr.epf.restaurant.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Configuration de la base de données H2 en mémoire.
 *
 * EmbeddedDatabaseBuilder exécute automatiquement schema.sql puis data.sql
 * à chaque démarrage du contexte Spring, garantissant un état initial propre.
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    /**
     * Démarre le serveur TCP H2 pour permettre une connexion externe si besoin.
     */
    @Bean
    public Server h2TcpServer() throws SQLException {
        Server server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
        try {
            server.start();
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        } catch (SQLException e) {
            if (e.getErrorCode() == 90061) { // port déjà utilisé
                System.out.println("[H2] TCP server non démarré : port 9092 déjà utilisé.");
            } else {
                throw e;
            }
        }
        return server;
    }

    /**
     * Crée la base H2 embarquée et charge les scripts SQL au démarrage.
     */
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("restaurantdb")
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
    }

    /**
     * Expose JdbcTemplate pour les DAO.
     */
    @Bean
    public JdbcTemplate jdbcTemplate(@NonNull DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Expose le gestionnaire de transactions utilisé par @Transactional.
     * En cas de RuntimeException, la transaction est automatiquement annulée (rollback).
     */
    @Bean
    public PlatformTransactionManager transactionManager(@NonNull DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
