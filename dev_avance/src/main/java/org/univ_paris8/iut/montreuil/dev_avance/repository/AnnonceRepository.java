package org.univ_paris8.iut.montreuil.dev_avance.repository;

import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce.Status;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Category;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.List;

public class AnnonceRepository {

    private final EntityManager em;

    public AnnonceRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Annonce annonce) {
        if (annonce.getDate() == null) {
            annonce.setDate(new Timestamp(System.currentTimeMillis()));
        }
        em.persist(annonce);
    }

    public void update(Annonce annonce) {
        em.merge(annonce);
    }

    public void delete(Annonce annonce) {
        em.remove(annonce);
    }

    public Annonce findById(Long id) {
        try {
            return em.createQuery(
                    "SELECT a FROM Annonce a LEFT JOIN FETCH a.category LEFT JOIN FETCH a.author WHERE a.id = :id",
                    Annonce.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Annonce> findAll(int page, int pageSize) {
        TypedQuery<Annonce> query = em.createQuery(
                "SELECT a FROM Annonce a LEFT JOIN FETCH a.category LEFT JOIN FETCH a.author ORDER BY a.date DESC",
                Annonce.class);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public long countAll() {
        return em.createQuery("SELECT COUNT(a) FROM Annonce a", Long.class).getSingleResult();
    }

    public List<Annonce> findByKeyword(String keyword, int page, int pageSize) {
        TypedQuery<Annonce> query = em.createQuery(
                "SELECT a FROM Annonce a LEFT JOIN FETCH a.category LEFT JOIN FETCH a.author WHERE lower(a.title) LIKE lower(:kw) OR lower(a.description) LIKE lower(:kw)",
                Annonce.class);
        query.setParameter("kw", "%" + keyword + "%");
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public List<Annonce> findByCategoryAndStatus(Category category, Status status, int page, int pageSize) {
        StringBuilder jpql = new StringBuilder("SELECT a FROM Annonce a WHERE 1=1");
        if (category != null)
            jpql.append(" AND a.category = :category");
        if (status != null)
            jpql.append(" AND a.status = :status");

        TypedQuery<Annonce> query = em.createQuery(jpql.toString(), Annonce.class);
        if (category != null)
            query.setParameter("category", category);
        if (status != null)
            query.setParameter("status", status);

        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }
}
