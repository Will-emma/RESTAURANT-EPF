package fr.epf.restaurant.exception;

/**
 * Exception utilisée quand le stock n'est pas suffisant.
 */
public class StockInsuffisantException extends RestaurantException {

    /**
     * Construit l'exception avec un message explicite.
     */
    public StockInsuffisantException(final String message) {
        super(message);
    }
}
