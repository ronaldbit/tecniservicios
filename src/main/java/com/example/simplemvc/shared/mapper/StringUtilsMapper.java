package com.example.simplemvc.shared.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtilsMapper {
  public static String trimString(String value) {
    return value != null ? value.trim() : null;
  }
}
