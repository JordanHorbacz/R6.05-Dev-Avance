package org.univ_paris8.iut.montreuil.dev_avance.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.univ_paris8.iut.montreuil.dev_avance.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Add mapping methods if DTO exists, otherwise just a placeholder or basic
    // mapping
    // Assuming we might need UserDTO later, but for now we might not have one
    // defined in the file list.
    // UserDTO was not in the file list. I'll just create a placeholder DTO or skip
    // if not needed.
    // Prompt says: "UserMapper (si DTO utilisateur)".
    // I'll create a simple UserDTO and Mapper if I want to be thorough, but I don't
    // see UserDTO in the file list.
    // I will assume for now we don't expose User yet, or I'll just leave it empty.
}
