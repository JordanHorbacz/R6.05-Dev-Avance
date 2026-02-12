package org.univ_paris8.iut.montreuil.dev_avance.test;

import org.junit.jupiter.api.*;
import org.testng.annotations.Test;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.repository.AnnonceRepository;
import org.univ_paris8.iut.montreuil.dev_avance.util.EntityManagerUtil;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnnonceRepositoryTest {
    private EntityManager em;
    private AnnonceRepository repo;

    @BeforeEach
    void setUp() {
        em = EntityManagerUtil.getEntityManager();
        repo = new AnnonceRepository(em);
        em.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }

    @Test
    void testSaveAndFindById() {
        Annonce a = new Annonce();
        a.setTitle("Annonce Test");
        repo.save(a);
        em.flush();

        Annonce found = repo.findById(a.getId());
        assertNotNull(found);
        assertEquals("Annonce Test", found.getTitle());
    }
}