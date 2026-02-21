package org.univ_paris8.iut.montreuil.dev_avance.dao;

import org.univ_paris8.iut.montreuil.dev_avance.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour les Catégories.
 * Définit le contrat d'accès aux données des catégories,
 * découplé de l'implémentation Spring Data JPA.
 */
public interface CategoryDao {

    Optional<Category> findById(Long id);

    Optional<Category> findByLabel(String label);

    List<Category> findAll();

    Category save(Category category);
}
