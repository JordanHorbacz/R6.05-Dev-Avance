package org.univ_paris8.iut.montreuil.dev_avance.dto;

import java.util.List;

/**
 * DTO de réponse retourné après une authentification réussie (POST
 * /api/auth/login).
 * Contient le token JWT et les informations basiques de l'utilisateur connecté.
 */
public class AuthResponseDTO {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private List<String> roles;

    public AuthResponseDTO(String token, Long id, String username, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
