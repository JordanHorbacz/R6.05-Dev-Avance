package org.univ_paris8.iut.montreuil.dev_avance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends ResourceNotFoundException {

    public CategoryNotFoundException(Long id) {
        super("Catégorie introuvable avec l'ID : " + id);
    }

    public CategoryNotFoundException(String label) {
        super("Catégorie introuvable : " + label);
    }
}
