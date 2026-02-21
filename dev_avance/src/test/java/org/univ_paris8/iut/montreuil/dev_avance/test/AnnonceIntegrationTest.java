package org.univ_paris8.iut.montreuil.dev_avance.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Category;
import org.univ_paris8.iut.montreuil.dev_avance.entity.User;
import org.univ_paris8.iut.montreuil.dev_avance.repository.AnnonceRepository;
import org.univ_paris8.iut.montreuil.dev_avance.repository.CategoryRepository;
import org.univ_paris8.iut.montreuil.dev_avance.repository.UserRepository;

import java.sql.Timestamp;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AnnonceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AnnonceRepository annonceRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        annonceRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("integrationUser");
        testUser.setEmail("integration@test.com");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRoles(Set.of("ROLE_USER"));
        userRepository.save(testUser);

        testCategory = new Category();
        testCategory.setLabel("Integration Category");
        categoryRepository.save(testCategory);
    }

    @Test
    void loginAvecBonIdentifiants_retourne200AvecToken() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        { "username": "integrationUser", "password": "password123" }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.username").value("integrationUser"));
    }

    @Test
    void accesProtegeSansToken_retourne401() throws Exception {
        mockMvc.perform(get("/api/annonces"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "integrationUser", roles = { "USER" })
    void creerAnnonce_retourneAnnonceDraft() throws Exception {
        mockMvc.perform(post("/api/annonces")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "Test annonce intégration",
                            "description": "Lancé dans Testcontainers",
                            "mail": "test@docker.com",
                            "categoryId": %d,
                            "authorId": %d
                        }
                        """.formatted(testCategory.getId(), testUser.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test annonce intégration"))
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    @WithMockUser(username = "integrationUser", roles = { "USER" })
    void rechercherAnnonces_retourneResultatPagine() throws Exception {
        Annonce annonce = new Annonce();
        annonce.setTitle("Cherche moi");
        annonce.setDescription("Description pour le test de recherche");
        annonce.setDate(new Timestamp(System.currentTimeMillis()));
        annonce.setStatus(Annonce.Status.PUBLISHED);
        annonce.setAuthor(testUser);
        annonce.setCategory(testCategory);
        annonceRepository.save(annonce);

        mockMvc.perform(get("/api/annonces?q=Cherche"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Cherche moi"));
    }
}
