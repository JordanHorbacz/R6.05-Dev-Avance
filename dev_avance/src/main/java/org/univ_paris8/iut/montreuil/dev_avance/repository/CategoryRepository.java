package org.univ_paris8.iut.montreuil.dev_avance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByLabel(String label);
}
