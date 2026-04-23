package fr.epf.restaurant.dto;

/**
 * Représente un message d'erreur renvoyé par l'API.
 */
public class ErreurResponse {

    private int status;
    private String message;

    public ErreurResponse() {
    }

    public ErreurResponse(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
