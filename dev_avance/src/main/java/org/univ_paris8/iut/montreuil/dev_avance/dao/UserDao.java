package org.univ_paris8.iut.montreuil.dev_avance.dao;

import org.univ_paris8.iut.montreuil.dev_avance.entity.User;

import java.util.Optional;

/**
 * Interface DAO pour les Utilisateurs.
 * Définit le contrat d'accès aux données utilisateur,
 * découplé de l'implémentation Spring Data JPA.
 */
public interface UserDao {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User save(User user);
}
