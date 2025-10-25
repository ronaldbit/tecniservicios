package com.example.simplemvc.shared.mapper;

public interface BuilderMapper<T, B> {
  B toBuilder(T type);
}
