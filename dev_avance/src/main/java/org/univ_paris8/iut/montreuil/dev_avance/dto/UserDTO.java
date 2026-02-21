package org.univ_paris8.iut.montreuil.dev_avance.dto;

import jakarta.validation.constraints.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * DTO de réponse pour un utilisateur.
 * Utilisé uniquement pour exposer les informations publiques d'un utilisateur.
 * Le mot de passe n'est JAMAIS inclus ici.
 */
public class UserDTO {

    private Long id;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    private String username;

    @NotBlank(message = "L'adresse email est obligatoire")
    @Email(message = "L'adresse email doit être valide")
    @Size(max = 100, message = "L'adresse email ne peut pas dépasser 100 caractères")
    private String email;

    private Timestamp createdAt;

    private Set<String> roles;

    public UserDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
