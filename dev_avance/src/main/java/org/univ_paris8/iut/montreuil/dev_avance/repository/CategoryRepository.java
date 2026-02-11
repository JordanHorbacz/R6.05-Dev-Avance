package org.univ_paris8.iut.montreuil.dev_avance.repository;

import org.univ_paris8.iut.montreuil.dev_avance.entity.Category;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CategoryRepository {

    private final EntityManager em;

    public CategoryRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Category category) {
        em.persist(category);
    }

    public void update(Category category) {
        em.merge(category);
    }

    public Category findById(Long id) {
        return em.find(Category.class, id);
    }

    public List<Category> findAll() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    public void delete(Category category) {
        em.remove(category);
    }
}
