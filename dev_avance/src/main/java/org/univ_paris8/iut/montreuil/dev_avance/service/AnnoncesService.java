package org.univ_paris8.iut.montreuil.dev_avance.service;

import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce.Status;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Category;
import org.univ_paris8.iut.montreuil.dev_avance.entity.User;
import org.univ_paris8.iut.montreuil.dev_avance.repository.AnnonceRepository;
import org.univ_paris8.iut.montreuil.dev_avance.util.EntityManagerUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.Timestamp;
import java.util.List;

public class AnnoncesService {

    public void createAnnonce(Annonce annonce, Long categoryId, Long userId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            if (categoryId != null) {
                annonce.setCategory(em.find(Category.class, categoryId));
            }
            if (userId != null) {
                annonce.setAuthor(em.find(User.class, userId));
            }

            annonce.setDate(new Timestamp(System.currentTimeMillis()));
            annonce.setStatus(Status.DRAFT);

            em.persist(annonce);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Annonce getAnnonce(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            AnnonceRepository repo = new AnnonceRepository(em);
            Annonce annonce = repo.findById(id);
            if (annonce != null) {
                if (annonce.getCategory() != null) annonce.getCategory().getLabel();
                if (annonce.getAuthor() != null) annonce.getAuthor().getUsername();
            }
            return annonce;
        } finally {
            em.close();
        }
    }


    public void updateAnnonce(Annonce annonce) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(annonce);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void publishAnnonce(Long id) {
        updateStatus(id, Status.PUBLISHED);
    }

    public void archiveAnnonce(Long id) {
        updateStatus(id, Status.ARCHIVED);
    }

    private void updateStatus(Long id, Status status) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Annonce annonce = em.find(Annonce.class, id);
            if (annonce != null) {
                annonce.setStatus(status);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteAnnonce(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Annonce annonce = em.find(Annonce.class, id);
            if (annonce != null) {
                em.remove(annonce);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Annonce> getAnnonces(int page, int pageSize) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            return new AnnonceRepository(em).findAll(page, pageSize);
        } finally {
            em.close();
        }
    }

    public List<Annonce> searchAnnonces(String keyword, int page, int pageSize) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            return new AnnonceRepository(em).findByKeyword(keyword, page, pageSize);
        } finally {
            em.close();
        }
    }

    public List<Category> getAllCategories() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        } finally {
            em.close();
        }
    }
}