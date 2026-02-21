package org.univ_paris8.iut.montreuil.dev_avance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(Long id) {
        super("Utilisateur introuvable avec l'ID : " + id);
    }

    public UserNotFoundException(String username) {
        super("Utilisateur introuvable avec le nom d'utilisateur : " + username);
    }
}
