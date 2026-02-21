package org.univ_paris8.iut.montreuil.dev_avance.mapper;

import org.mapstruct.Mapper;
import org.univ_paris8.iut.montreuil.dev_avance.dto.UserDTO;
import org.univ_paris8.iut.montreuil.dev_avance.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);
}
