package fr.epf.restaurant.exception;

/**
 * Exception utilisée quand les données reçues sont invalides.
 */
public class DonneesInvalidesException extends RestaurantException {

    /**
     * Construit l'exception avec un message explicite.
     */
    public DonneesInvalidesException(final String message) {
        super(message);
    }
}
