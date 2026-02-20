package org.univ_paris8.iut.montreuil.dev_avance.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.univ_paris8.iut.montreuil.dev_avance.dto.AnnonceDTO;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;

@Mapper(componentModel = "spring", uses = { UserMapper.class, CategoryMapper.class })
public interface AnnonceMapper {

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.username")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryLabel", source = "category.label")
    AnnonceDTO toDTO(Annonce annonce);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "category", ignore = true)
    Annonce toEntity(AnnonceDTO annonceDTO);

    @org.mapstruct.BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true) // Do not update ID
    void updateAnnonceFromDto(AnnonceDTO dto, @org.mapstruct.MappingTarget Annonce entity);
}
