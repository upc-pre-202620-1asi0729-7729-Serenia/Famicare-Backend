package com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa.converters;

import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ZoneRadius;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ZoneRadiusAttributeConverter implements AttributeConverter<ZoneRadius, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ZoneRadius attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public ZoneRadius convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : new ZoneRadius(dbData);
    }
}
