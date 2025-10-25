package com.example.simplemvc.shared.utils.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.example.simplemvc.model.UsuarioRol;

import jakarta.persistence.Converter;

@Converter
public class ListUsuarioRolAttributeConverter implements ListEnumAttributeConverter<UsuarioRol> {
  @Override
  public String convertToDatabaseColumn(List<UsuarioRol> attribute) {
    if (attribute == null) {
      return null;
    }

    return attribute.stream()
        .map(UsuarioRol::name)
        .collect(Collectors.joining(","));
  }

  @Override
  public List<UsuarioRol> convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return List.of();
    }

    return List.of(dbData.split(","))
        .stream()
        .map(UsuarioRol::valueOf)
        .toList();
  }
}
