package org.univ_paris8.iut.montreuil.dev_avance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de réponse pour une catégorie.
 * Utilisé pour exposer les informations d'une catégorie côté API.
 */
public class CategoryDTO {

    private Long id;

    @NotBlank(message = "Le libellé de la catégorie est obligatoire")
    @Size(min = 2, max = 64, message = "Le libellé doit contenir entre 2 et 64 caractères")
    private String label;

    public CategoryDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
