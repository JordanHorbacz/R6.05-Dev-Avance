package org.univ_paris8.iut.montreuil.dev_avance.dto;

import jakarta.validation.constraints.*;
import java.sql.Timestamp;

/**
 * DTO de réponse pour une annonce.
 * Utilisé pour retourner les données d'une annonce au client (GET).
 * Contient les informations de lecture seule (id, authorName, categoryLabel,
 * etc.)
 */
public class AnnonceDTO {

    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 64, message = "Le titre doit contenir entre 3 et 64 caractères")
    private String title;

    @Size(max = 256, message = "La description ne peut pas dépasser 256 caractères")
    private String description;

    @Size(max = 64, message = "L'adresse ne peut pas dépasser 64 caractères")
    private String adress;

    @Email(message = "L'adresse mail doit être valide")
    @Size(max = 64, message = "L'adresse mail ne peut pas dépasser 64 caractères")
    private String mail;

    private Timestamp date;

    private String status;

    private String authorName;

    private String categoryLabel;

    @NotNull(message = "L'identifiant de la catégorie est obligatoire")
    @Positive(message = "L'identifiant de la catégorie doit être un nombre positif")
    private Long categoryId;

    @Positive(message = "L'identifiant de l'auteur doit être un nombre positif")
    private Long authorId;

    public AnnonceDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
