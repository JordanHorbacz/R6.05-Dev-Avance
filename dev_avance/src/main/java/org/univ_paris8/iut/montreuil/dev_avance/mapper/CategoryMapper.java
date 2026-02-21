package org.univ_paris8.iut.montreuil.dev_avance.mapper;

import org.mapstruct.Mapper;
import org.univ_paris8.iut.montreuil.dev_avance.dto.CategoryDTO;
import org.univ_paris8.iut.montreuil.dev_avance.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category category);

    Category toEntity(CategoryDTO categoryDTO);
}
