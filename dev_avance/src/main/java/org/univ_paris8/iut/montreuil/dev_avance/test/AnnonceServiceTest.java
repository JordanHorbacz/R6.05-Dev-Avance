package org.univ_paris8.iut.montreuil.dev_avance.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce.Status;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Category;
import org.univ_paris8.iut.montreuil.dev_avance.entity.User;
import org.univ_paris8.iut.montreuil.dev_avance.service.AnnoncesService;
import org.univ_paris8.iut.montreuil.dev_avance.util.EntityManagerUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnnonceServiceTest {

    @Mock
    private EntityManager em;

    @Mock
    private EntityTransaction tx;

    @InjectMocks
    private AnnoncesService annoncesService;

    @BeforeEach
    void setUp() {
        lenient().when(em.getTransaction()).thenReturn(tx);
    }

    @Test
    void testCreateAnnonceStatusAndDate() {
        try (MockedStatic<EntityManagerUtil> emUtilMock = mockStatic(EntityManagerUtil.class)) {
            emUtilMock.when(EntityManagerUtil::getEntityManager).thenReturn(em);

            Annonce annonce = new Annonce();
            Category mockCat = new Category();
            User mockUser = new User();

            when(em.find(Category.class, 1L)).thenReturn(mockCat);
            when(em.find(User.class, 1L)).thenReturn(mockUser);

            annoncesService.createAnnonce(annonce, 1L, 1L);

            assertEquals(Status.DRAFT, annonce.getStatus());
            assertNotNull(annonce.getDate());
            assertEquals(mockCat, annonce.getCategory());
            assertEquals(mockUser, annonce.getAuthor());

            verify(em).persist(annonce);
            verify(tx).commit();
        }
    }

    @Test
    void testUpdateStatusToPublished() {
        try (MockedStatic<EntityManagerUtil> emUtilMock = mockStatic(EntityManagerUtil.class)) {
            emUtilMock.when(EntityManagerUtil::getEntityManager).thenReturn(em);

            Annonce existingAnnonce = new Annonce();
            existingAnnonce.setStatus(Status.DRAFT);
            when(em.find(Annonce.class, 10L)).thenReturn(existingAnnonce);

            annoncesService.publishAnnonce(10L);

            assertEquals(Status.PUBLISHED, existingAnnonce.getStatus());
            verify(tx).commit();
        }
    }

    @Test
    void testRollbackOnException() {
        try (MockedStatic<EntityManagerUtil> emUtilMock = mockStatic(EntityManagerUtil.class)) {
            emUtilMock.when(EntityManagerUtil::getEntityManager).thenReturn(em);

            doThrow(new RuntimeException()).when(em).persist(any());
            when(tx.isActive()).thenReturn(true);

            assertThrows(RuntimeException.class, () -> {
                annoncesService.createAnnonce(new Annonce(), null, null);
            });

            verify(tx).rollback();
        }
    }
}