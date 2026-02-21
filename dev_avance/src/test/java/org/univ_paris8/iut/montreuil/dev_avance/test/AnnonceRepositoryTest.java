package org.univ_paris8.iut.montreuil.dev_avance.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Category;
import org.univ_paris8.iut.montreuil.dev_avance.entity.User;
import org.univ_paris8.iut.montreuil.dev_avance.repository.AnnonceRepository;
import org.univ_paris8.iut.montreuil.dev_avance.repository.AnnonceSpecifications;
import org.univ_paris8.iut.montreuil.dev_avance.repository.CategoryRepository;
import org.univ_paris8.iut.montreuil.dev_avance.repository.UserRepository;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
class AnnonceRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test-repo-db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private AnnonceRepository annonceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User auteur;
    private Category categorie;

    @BeforeEach
    void setUp() {
        annonceRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        auteur = new User();
        auteur.setUsername("repoTestUser");
        auteur.setEmail("repo@test.com");
        auteur.setPassword("encodedPassword");
        userRepository.save(auteur);

        categorie = new Category();
        categorie.setLabel("Test Categorie");
        categoryRepository.save(categorie);
    }

    @Test
    void sauvegarderEtRecuperer() {
        Annonce annonce = buildAnnonce("Annonce de test repository", Annonce.Status.DRAFT);
        annonceRepository.save(annonce);

        Optional<Annonce> resultat = annonceRepository.findById(annonce.getId());
        assertTrue(resultat.isPresent());
        assertEquals("Annonce de test repository", resultat.get().getTitle());
    }

    @Test
    void rechercherParMotCle() {
        annonceRepository.save(buildAnnonce("Appartement à louer Paris", Annonce.Status.PUBLISHED));

        Specification<Annonce> spec = AnnonceSpecifications.withFilters("appartement", null, null, null, null, null);
        Page<Annonce> page = annonceRepository.findAll(spec, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("Appartement à louer Paris", page.getContent().get(0).getTitle());
    }

    @Test
    void rechercherParStatut() {
        annonceRepository.save(buildAnnonce("Brouillon", Annonce.Status.DRAFT));
        annonceRepository.save(buildAnnonce("Publiée", Annonce.Status.PUBLISHED));

        Specification<Annonce> spec = AnnonceSpecifications.withFilters(null, "PUBLISHED", null, null, null, null);
        Page<Annonce> page = annonceRepository.findAll(spec, PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("Publiée", page.getContent().get(0).getTitle());
    }

    private Annonce buildAnnonce(String title, Annonce.Status status) {
        Annonce a = new Annonce();
        a.setTitle(title);
        a.setStatus(status);
        a.setAuthor(auteur);
        a.setCategory(categorie);
        a.setDate(new Timestamp(System.currentTimeMillis()));
        return a;
    }
}