package org.univ_paris8.iut.montreuil.dev_avance;

import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.util.EntityManagerUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class AnnonceDAO extends DAO<Annonce> {

    public AnnonceDAO() {
    }

    @Override
    public boolean create(Annonce obj) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(obj);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Annonce> findAll() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Annonce a", Annonce.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Annonce find(int id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            return em.find(Annonce.class, (long) id);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean update(Annonce obj) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(obj);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(int id) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Annonce a = em.find(Annonce.class, (long) id);
            if (a != null) {
                em.remove(a);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}