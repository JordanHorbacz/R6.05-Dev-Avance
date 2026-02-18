package org.univ_paris8.iut.montreuil.dev_avance.mapper;

import org.univ_paris8.iut.montreuil.dev_avance.dto.AnnonceDTO;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce.Status;

public class AnnonceMapper {

    public static AnnonceDTO toDTO(Annonce entity) {
        if (entity == null) {
            return null;
        }
        return AnnonceDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .adress(entity.getAdress())
                .mail(entity.getMail())
                .date(entity.getDate())
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .authorName(entity.getAuthor() != null ? entity.getAuthor().getUsername() : null)
                .categoryLabel(entity.getCategory() != null ? entity.getCategory().getLabel() : null)
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .authorId(entity.getAuthor() != null ? entity.getAuthor().getId() : null)
                .build();
    }

    public static Annonce toEntity(AnnonceDTO dto) {
        if (dto == null) {
            return null;
        }
        Annonce entity = new Annonce();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setAdress(dto.getAdress());
        entity.setMail(dto.getMail());
        entity.setMail(dto.getMail());

        if (dto.getDate() != null) {
            entity.setDate(dto.getDate());
        }
        if (dto.getStatus() != null) {
            try {
                entity.setStatus(Status.valueOf(dto.getStatus()));
            } catch (IllegalArgumentException e) {
                entity.setStatus(Status.DRAFT);
            }
        }
        return entity;
    }

    public static void updateEntity(Annonce entity, AnnonceDTO dto) {
        if (dto == null || entity == null) {
            return;
        }
        if (dto.getTitle() != null)
            entity.setTitle(dto.getTitle());
        if (dto.getDescription() != null)
            entity.setDescription(dto.getDescription());
        if (dto.getAdress() != null)
            entity.setAdress(dto.getAdress());
        if (dto.getMail() != null)
            entity.setMail(dto.getMail());
        if (dto.getDate() != null)
            entity.setDate(dto.getDate());

        if (dto.getStatus() != null) {
            try {
                entity.setStatus(Status.valueOf(dto.getStatus()));
            } catch (IllegalArgumentException e) {
            }
        }
    }
}
