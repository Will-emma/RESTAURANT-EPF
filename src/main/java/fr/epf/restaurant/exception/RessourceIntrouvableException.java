package fr.epf.restaurant.exception;

/**
 * Exception utilisée quand une ressource demandée n'existe pas.
 */
public class RessourceIntrouvableException extends RestaurantException {

    /**
     * Construit l'exception avec un message explicite.
     */
    public RessourceIntrouvableException(final String message) {
        super(message);
    }
}
