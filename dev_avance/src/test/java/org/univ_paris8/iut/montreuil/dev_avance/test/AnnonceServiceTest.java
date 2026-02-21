package org.univ_paris8.iut.montreuil.dev_avance.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.univ_paris8.iut.montreuil.dev_avance.dto.AnnonceDTO;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Category;
import org.univ_paris8.iut.montreuil.dev_avance.entity.User;
import org.univ_paris8.iut.montreuil.dev_avance.exception.AnnonceNotModifiableException;
import org.univ_paris8.iut.montreuil.dev_avance.exception.CategoryNotFoundException;
import org.univ_paris8.iut.montreuil.dev_avance.exception.ResourceNotFoundException;
import org.univ_paris8.iut.montreuil.dev_avance.exception.UnauthorizedActionException;
import org.univ_paris8.iut.montreuil.dev_avance.exception.UserNotFoundException;
import org.univ_paris8.iut.montreuil.dev_avance.mapper.AnnonceMapper;
import org.univ_paris8.iut.montreuil.dev_avance.repository.AnnonceRepository;
import org.univ_paris8.iut.montreuil.dev_avance.repository.CategoryRepository;
import org.univ_paris8.iut.montreuil.dev_avance.repository.UserRepository;
import org.univ_paris8.iut.montreuil.dev_avance.service.AnnonceService;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnonceServiceTest {

    @Mock
    private AnnonceRepository annonceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AnnonceMapper annonceMapper;

    @InjectMocks
    private AnnonceService annonceService;

    private User auteur;
    private Category categorie;
    private Annonce annonce;
    private AnnonceDTO annonceDTO;

    @BeforeEach
    void setUp() {
        auteur = new User();
        auteur.setId(1L);
        auteur.setUsername("testUser");

        categorie = new Category();
        categorie.setId(2L);
        categorie.setLabel("Immobilier");

        annonce = new Annonce();
        annonce.setId(10L);
        annonce.setTitle("Mon annonce test");
        annonce.setStatus(Annonce.Status.DRAFT);
        annonce.setAuthor(auteur);
        annonce.setCategory(categorie);

        annonceDTO = new AnnonceDTO();
        annonceDTO.setTitle("Mon annonce test");
        annonceDTO.setAuthorId(1L);
        annonceDTO.setCategoryId(2L);
    }

    @Test
    void creerAnnonce_devrait_retournerDraftAvecDateRenseignee() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(auteur));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(categorie));
        when(annonceMapper.toEntity(annonceDTO)).thenReturn(annonce);
        when(annonceRepository.save(any(Annonce.class))).thenReturn(annonce);
        when(annonceMapper.toDTO(annonce)).thenReturn(annonceDTO);

        AnnonceDTO resultat = annonceService.createAnnonce(annonceDTO);

        assertNotNull(resultat);
        assertEquals(Annonce.Status.DRAFT, annonce.getStatus());
        assertNotNull(annonce.getDate());
        verify(annonceRepository).save(annonce);
    }

    @Test
    void creerAnnonce_auteurIntrouvable_devrait_leverException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        annonceDTO.setAuthorId(99L);

        assertThrows(UserNotFoundException.class, () -> annonceService.createAnnonce(annonceDTO));
    }

    @Test
    void creerAnnonce_categorieIntrouvable_devrait_leverException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(auteur));
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        annonceDTO.setCategoryId(99L);

        assertThrows(CategoryNotFoundException.class, () -> annonceService.createAnnonce(annonceDTO));
    }

    @Test
    void recupererAnnonce_introuvable_devrait_leverException() {
        when(annonceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> annonceService.getAnnonce(999L));
    }

    @Test
    void mettreAJourAnnonce_publiee_devrait_leverAnnonceNotModifiable() {
        annonce.setStatus(Annonce.Status.PUBLISHED);
        when(annonceRepository.findById(10L)).thenReturn(Optional.of(annonce));

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testUser");
        when(auth.getAuthorities()).thenAnswer(inv -> Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        assertThrows(AnnonceNotModifiableException.class,
                () -> annonceService.updateAnnonce(10L, annonceDTO));

        SecurityContextHolder.clearContext();
    }

    @Test
    void mettreAJourAnnonce_pasAuteur_devrait_leverUnauthorized() {
        when(annonceRepository.findById(10L)).thenReturn(Optional.of(annonce));

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("autreUser");
        when(auth.getAuthorities()).thenAnswer(inv -> Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        assertThrows(UnauthorizedActionException.class,
                () -> annonceService.updateAnnonce(10L, annonceDTO));

        SecurityContextHolder.clearContext();
    }

    @Test
    void supprimerAnnonce_auteur_devrait_supprimer() {
        when(annonceRepository.findById(10L)).thenReturn(Optional.of(annonce));

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testUser");
        when(auth.getAuthorities()).thenAnswer(inv -> Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        annonceService.deleteAnnonce(10L);

        verify(annonceRepository).delete(annonce);
        SecurityContextHolder.clearContext();
    }
}