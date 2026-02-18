package org.univ_paris8.iut.montreuil.dev_avance.service;

import org.univ_paris8.iut.montreuil.dev_avance.entity.User;
import org.univ_paris8.iut.montreuil.dev_avance.repository.UserRepository;
import org.univ_paris8.iut.montreuil.dev_avance.util.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.util.Optional;

public class UserService {

    public User login(String email, String password) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            UserRepository repo = new UserRepository(em);
            Optional<User> userOpt = repo.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (user.getPassword().equals(password)) {
                    return user;
                }
            }
            return null;
        } finally {
            em.close();
        }
    }

    public User loginByUsername(String username, String password) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            UserRepository repo = new UserRepository(em);
            Optional<User> userOpt = repo.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (user.getPassword().equals(password)) {
                    return user;
                }
            }
            return null;
        } finally {
            em.close();
        }
    }

    public void register(User user) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            UserRepository repo = new UserRepository(em);
            repo.save(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void initTestUser() {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            UserRepository repo = new UserRepository(em);
            if (!repo.findByEmail("test@test.com").isPresent()) {
                em.getTransaction().begin();

                User testUser = new User("TestUser", "test@test.com", "test1234");
                repo.save(testUser);

                em.getTransaction().commit();
                System.out.println(">>> Utilisateur de test (test@test.com / test) créé avec succès !");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void createUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        UserRepository.save(user);
    }
}