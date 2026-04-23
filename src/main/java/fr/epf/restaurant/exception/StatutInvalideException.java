package fr.epf.restaurant.exception;

/**
 * Exception utilisée quand une transition de statut est interdite.
 */
public class StatutInvalideException extends RestaurantException {

    /**
     * Construit l'exception avec un message explicite.
     */
    public StatutInvalideException(final String message) {
        super(message);
    }
}
