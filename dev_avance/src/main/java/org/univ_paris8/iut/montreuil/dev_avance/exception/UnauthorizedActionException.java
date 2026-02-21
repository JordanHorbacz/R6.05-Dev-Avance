package org.univ_paris8.iut.montreuil.dev_avance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedActionException extends RuntimeException {

    public UnauthorizedActionException(String action) {
        super("Action refusée : vous n'avez pas le droit d'effectuer cette opération (" + action + ").");
    }

    public UnauthorizedActionException() {
        super("Action refusée : vous n'avez pas les droits nécessaires.");
    }
}
