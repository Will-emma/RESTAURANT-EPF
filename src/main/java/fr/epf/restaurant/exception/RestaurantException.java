package fr.epf.restaurant.exception;

/**
 * Exception métier de base du projet.
 */
public class RestaurantException extends RuntimeException {

    /**
     * Construit une exception avec un message lisible.
     */
    public RestaurantException(final String message) {
        super(message);
    }
}
