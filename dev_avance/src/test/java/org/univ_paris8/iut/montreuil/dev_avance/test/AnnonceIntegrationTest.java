package org.univ_paris8.iut.montreuil.dev_avance.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AnnonceIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void postgreSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
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

    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        annonceRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        // Create user
        testUser = new User();
        testUser.setUsername("integrationUser");
        testUser.setEmail("integration@test.com");
        testUser.setPassword("password"); // In real test, this should be encoded potentially if using real auth flow
        userRepository.save(testUser);

        // Create category
        testCategory = new Category();
        testCategory.setLabel("Integration Category");
        categoryRepository.save(testCategory);
    }

    @Test
    @WithMockUser(username = "integrationUser", roles = { "USER" })
    void shouldCreateAnnonce() throws Exception {
        String annonceJson = """
                {
                    "title": "Integration Test Annonce",
                    "description": "Running inside Testcontainers!",
                    "adress": "Docker Container",
                    "mail": "test@docker.com",
                    "categoryId": %d,
                    "authorId": %d
                }
                """.formatted(testCategory.getId(), testUser.getId());

        mockMvc.perform(post("/api/annonces")
                .contentType(MediaType.APPLICATION_JSON)
                .content(annonceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Test Annonce"))
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    @WithMockUser(username = "integrationUser", roles = { "USER" })
    void shouldSearchAnnonce() throws Exception {
        // Given
        Annonce annonce = new Annonce();
        annonce.setTitle("Search me");
        annonce.setDescription("Description for search test");
        annonce.setDate(new Timestamp(System.currentTimeMillis()));
        annonce.setStatus(Annonce.Status.PUBLISHED);
        annonce.setAuthor(testUser);
        annonce.setCategory(testCategory);
        annonceRepository.save(annonce);

        // When/Then
        mockMvc.perform(get("/api/annonces?q=Search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Search me"));
    }
}
