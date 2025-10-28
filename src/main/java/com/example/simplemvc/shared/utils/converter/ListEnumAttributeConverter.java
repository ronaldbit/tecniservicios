package com.example.simplemvc.shared.utils.converter;

import jakarta.persistence.AttributeConverter;
import java.util.List;

public interface ListEnumAttributeConverter<T extends Enum<T>>
    extends AttributeConverter<List<T>, String> {}
