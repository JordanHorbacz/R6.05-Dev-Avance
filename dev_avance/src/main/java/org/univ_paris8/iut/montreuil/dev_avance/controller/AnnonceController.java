package org.univ_paris8.iut.montreuil.dev_avance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.univ_paris8.iut.montreuil.dev_avance.dto.AnnonceDTO;
import org.univ_paris8.iut.montreuil.dev_avance.service.AnnonceService;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/annonces")
@SecurityRequirement(name = "bearerAuth")
public class AnnonceController {

    private final AnnonceService annonceService;

    public AnnonceController(AnnonceService annonceService) {
        this.annonceService = annonceService;
    }

    @GetMapping
    @Operation(summary = "Get annonces with filters and pagination")
    public ResponseEntity<Page<AnnonceDTO>> getAllAnnonces(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Timestamp fromDate,
            @RequestParam(required = false) Timestamp toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity
                .ok(annonceService.searchAnnonces(q, status, categoryId, authorId, fromDate, toDate, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnonceDTO> getAnnonceById(@PathVariable Long id) {
        return ResponseEntity.ok(annonceService.getAnnonce(id));
    }

    @PostMapping
    public ResponseEntity<AnnonceDTO> createAnnonce(@Valid @RequestBody AnnonceDTO annonceDTO) {
        return ResponseEntity.ok(annonceService.createAnnonce(annonceDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnonceDTO> updateAnnonce(@PathVariable Long id, @Valid @RequestBody AnnonceDTO annonceDTO) {
        return ResponseEntity.ok(annonceService.updateAnnonce(id, annonceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        annonceService.deleteAnnonce(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AnnonceDTO> patchAnnonce(@PathVariable Long id, @RequestBody AnnonceDTO annonceDTO) {
        // Bonus implementation
        return ResponseEntity.ok(annonceService.updateAnnonce(id, annonceDTO));
    }
}
