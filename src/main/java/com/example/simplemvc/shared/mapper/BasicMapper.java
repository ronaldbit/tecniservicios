package com.example.simplemvc.shared.mapper;

public interface BasicMapper<E, DTO> {
  DTO toDto(E domain);
}
