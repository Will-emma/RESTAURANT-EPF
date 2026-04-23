package fr.epf.restaurant.controller;

import fr.epf.restaurant.dto.ErreurResponse;
import fr.epf.restaurant.exception.DonneesInvalidesException;
import fr.epf.restaurant.exception.RessourceIntrouvableException;
import fr.epf.restaurant.exception.StatutInvalideException;
import fr.epf.restaurant.exception.StockInsuffisantException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * Transforme une ressource absente en réponse HTTP 404.
     */
    @ExceptionHandler(RessourceIntrouvableException.class)
    public ResponseEntity<ErreurResponse> handleNotFound(final RessourceIntrouvableException e) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /**
     * Transforme une erreur de saisie en réponse HTTP 400.
     */
    @ExceptionHandler(DonneesInvalidesException.class)
    public ResponseEntity<ErreurResponse> handleBadRequest(final DonneesInvalidesException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Transforme une transition de statut interdite en réponse HTTP 400.
     */
    @ExceptionHandler(StatutInvalideException.class)
    public ResponseEntity<ErreurResponse> handleStatut(final StatutInvalideException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Transforme un manque de stock en réponse HTTP 422.
     */
    @ExceptionHandler(StockInsuffisantException.class)
    public ResponseEntity<ErreurResponse> handleStock(final StockInsuffisantException e) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    /**
     * Gère les autres erreurs non prévues.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErreurResponse> handleOther(final Exception e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * Construit l'objet d'erreur renvoyé par l'API.
     */
    private ResponseEntity<ErreurResponse> buildResponse(final HttpStatus status, final String message) {
        ErreurResponse body = new ErreurResponse(status.value(), message);
        return ResponseEntity.status(status).body(body);
    }
}
