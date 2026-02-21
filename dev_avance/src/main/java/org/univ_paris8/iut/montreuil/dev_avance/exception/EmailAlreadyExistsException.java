package org.univ_paris8.iut.montreuil.dev_avance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("L'adresse email '" + email + "' est déjà utilisée.");
    }
}
