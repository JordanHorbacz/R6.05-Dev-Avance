package org.univ_paris8.iut.montreuil.dev_avance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AnnonceNotModifiableException extends RuntimeException {

    public AnnonceNotModifiableException(Long id) {
        super("L'annonce #" + id + " est publiée et ne peut plus être modifiée.");
    }

    public AnnonceNotModifiableException(String message) {
        super(message);
    }
}
