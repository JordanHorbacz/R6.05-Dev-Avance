package org.univ_paris8.iut.montreuil.dev_avance.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import org.univ_paris8.iut.montreuil.dev_avance.repository.AnnonceSpecifications;
import org.univ_paris8.iut.montreuil.dev_avance.repository.CategoryRepository;
import org.univ_paris8.iut.montreuil.dev_avance.repository.UserRepository;

import java.sql.Timestamp;

@Service
public class AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AnnonceMapper annonceMapper;

    public AnnonceService(AnnonceRepository annonceRepository, UserRepository userRepository,
            CategoryRepository categoryRepository, AnnonceMapper annonceMapper) {
        this.annonceRepository = annonceRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.annonceMapper = annonceMapper;
    }

    @Transactional
    public AnnonceDTO createAnnonce(AnnonceDTO dto) {
        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException(dto.getAuthorId()));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));

        Annonce annonce = annonceMapper.toEntity(dto);
        annonce.setAuthor(author);
        annonce.setCategory(category);
        annonce.setDate(new Timestamp(System.currentTimeMillis()));
        annonce.setStatus(Annonce.Status.DRAFT);

        return annonceMapper.toDTO(annonceRepository.save(annonce));
    }

    @Transactional(readOnly = true)
    public AnnonceDTO getAnnonce(Long id) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce introuvable avec l'ID : " + id));
        return annonceMapper.toDTO(annonce);
    }

    @Transactional
    public AnnonceDTO updateAnnonce(Long id, AnnonceDTO dto) {
        Annonce existingAnnonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce introuvable avec l'ID : " + id));

        verifierDroitModification(existingAnnonce);

        if (existingAnnonce.getStatus() == Annonce.Status.PUBLISHED) {
            throw new AnnonceNotModifiableException(id);
        }

        annonceMapper.updateAnnonceFromDto(dto, existingAnnonce);

        return annonceMapper.toDTO(annonceRepository.save(existingAnnonce));
    }

    @Transactional
    public void deleteAnnonce(Long id) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce introuvable avec l'ID : " + id));
        verifierDroitModification(annonce);
        annonceRepository.delete(annonce);
    }

    @Transactional(readOnly = true)
    public Page<AnnonceDTO> searchAnnonces(String q, String status, Long categoryId, Long authorId,
            Timestamp fromDate, Timestamp toDate, Pageable pageable) {
        Specification<Annonce> spec = AnnonceSpecifications.withFilters(q, status, categoryId, authorId, fromDate,
                toDate);
        return annonceRepository.findAll(spec, pageable).map(annonceMapper::toDTO);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void archiveAnnonce(Long id) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce introuvable avec l'ID : " + id));
        annonce.setStatus(Annonce.Status.ARCHIVED);
        annonceRepository.save(annonce);
    }

    @Transactional
    public void publishAnnonce(Long id) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce introuvable avec l'ID : " + id));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!annonce.getAuthor().getUsername().equals(currentUsername)) {
            throw new UnauthorizedActionException("seul l'auteur peut publier son annonce");
        }
        annonce.setStatus(Annonce.Status.PUBLISHED);
        annonceRepository.save(annonce);
    }

    /**
     * Vérifie que l'utilisateur courant a le droit de modifier ou supprimer cette
     * annonce.
     * Seuls l'auteur de l'annonce ou un administrateur peuvent agir dessus.
     */
    private void verifierDroitModification(Annonce annonce) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean estAuteur = annonce.getAuthor().getUsername().equals(currentUsername);
        boolean estAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!estAuteur && !estAdmin) {
            throw new UnauthorizedActionException("seul l'auteur ou un administrateur peut modifier cette annonce");
        }
    }
}
