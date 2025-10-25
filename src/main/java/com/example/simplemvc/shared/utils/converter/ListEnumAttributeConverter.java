package com.example.simplemvc.shared.utils.converter;

import java.util.List;

import jakarta.persistence.AttributeConverter;

public interface ListEnumAttributeConverter<T extends Enum<T>> extends AttributeConverter<List<T>, String> {
}
