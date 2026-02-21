package org.univ_paris8.iut.montreuil.dev_avance.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.repository.AnnonceRepository;

import java.util.Optional;

/**
 * Implémentation du DAO Annonce utilisant Spring Data JPA.
 * Fait le pont entre l'interface DAO et le repository Spring Data.
 */
@Component
public class AnnonceDaoImpl implements AnnonceDao {

    private final AnnonceRepository annonceRepository;

    public AnnonceDaoImpl(AnnonceRepository annonceRepository) {
        this.annonceRepository = annonceRepository;
    }

    @Override
    public Optional<Annonce> findById(Long id) {
        return annonceRepository.findById(id);
    }

    @Override
    public Page<Annonce> findAll(Specification<Annonce> spec, Pageable pageable) {
        return annonceRepository.findAll(spec, pageable);
    }

    @Override
    public Annonce save(Annonce annonce) {
        return annonceRepository.save(annonce);
    }

    @Override
    public void delete(Annonce annonce) {
        annonceRepository.delete(annonce);
    }

    @Override
    public boolean existsById(Long id) {
        return annonceRepository.existsById(id);
    }
}
