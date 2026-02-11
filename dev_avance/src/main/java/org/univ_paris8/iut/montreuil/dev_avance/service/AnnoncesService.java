package org.univ_paris8.iut.montreuil.dev_avance.service;

import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce.Status;
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
            AnnonceRepository repo = new AnnonceRepository(em);

            if (categoryId != null) {
                org.univ_paris8.iut.montreuil.dev_avance.entity.Category cat = em
                        .getReference(org.univ_paris8.iut.montreuil.dev_avance.entity.Category.class, categoryId);
                annonce.setCategory(cat);
            }
            if (userId != null) {
                org.univ_paris8.iut.montreuil.dev_avance.entity.User user = em
                        .getReference(org.univ_paris8.iut.montreuil.dev_avance.entity.User.class, userId);
                annonce.setAuthor(user);
            }

            annonce.setDate(new Timestamp(System.currentTimeMillis()));
            annonce.setStatus(Status.DRAFT);
            repo.save(annonce);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void updateAnnonce(Annonce annonce) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            AnnonceRepository repo = new AnnonceRepository(em);
            repo.update(annonce);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
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
            AnnonceRepository repo = new AnnonceRepository(em);
            Annonce annonce = repo.findById(id);
            if (annonce != null) {
                annonce.setStatus(status);

            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
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
            AnnonceRepository repo = new AnnonceRepository(em);
            Annonce annonce = repo.findById(id);
            if (annonce != null) {
                repo.delete(annonce);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Annonce getAnnonce(Long id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            AnnonceRepository repo = new AnnonceRepository(em);
            return repo.findById(id);
        } finally {
            em.close();
        }
    }

    public List<Annonce> getAnnonces(int page, int pageSize) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            AnnonceRepository repo = new AnnonceRepository(em);
            return repo.findAll(page, pageSize);
        } finally {
            em.close();
        }
    }

    public List<Annonce> searchAnnonces(String keyword, int page, int pageSize) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            AnnonceRepository repo = new AnnonceRepository(em);
            return repo.findByKeyword(keyword, page, pageSize);
        } finally {
            em.close();
        }
    }

    public List<org.univ_paris8.iut.montreuil.dev_avance.entity.Category> getAllCategories() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            return new org.univ_paris8.iut.montreuil.dev_avance.repository.CategoryRepository(em).findAll();
        } finally {
            em.close();
        }
    }
}
