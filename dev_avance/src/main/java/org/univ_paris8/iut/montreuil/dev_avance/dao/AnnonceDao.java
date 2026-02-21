package org.univ_paris8.iut.montreuil.dev_avance.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;

import java.util.Optional;

/**
 * Interface DAO (Data Access Object) pour les Annonces.
 * Définit le contrat d'accès aux données pour les annonces,
 * indépendamment de l'implémentation Spring Data JPA sous-jacente.
 *
 * Cette interface permet de découpler la couche service de l'implémentation
 * concrète du repository et facilite les tests unitaires.
 */
public interface AnnonceDao {

    Optional<Annonce> findById(Long id);

    Page<Annonce> findAll(Specification<Annonce> spec, Pageable pageable);

    Annonce save(Annonce annonce);

    void delete(Annonce annonce);

    boolean existsById(Long id);
}
