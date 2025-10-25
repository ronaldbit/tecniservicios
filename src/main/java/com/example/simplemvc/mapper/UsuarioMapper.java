package com.example.simplemvc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.security.core.userdetails.User;

import com.example.simplemvc.dto.UsuarioDto;
import com.example.simplemvc.shared.mapper.CrudMapper;
import com.example.simplemvc.shared.mapper.StringUtilsMapper;

@Mapper(componentModel = "spring", uses = { StringUtilsMapper.class })
public interface UsuarioMapper extends CrudMapper<User, UsuarioDto, User.UserBuilder> {

  @ObjectFactory
  default User.UserBuilder userBuilderFromDto(UsuarioDto dto) {
    return User.builder();
  }
}
