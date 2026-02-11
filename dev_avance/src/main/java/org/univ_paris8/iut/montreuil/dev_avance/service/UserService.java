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
}
