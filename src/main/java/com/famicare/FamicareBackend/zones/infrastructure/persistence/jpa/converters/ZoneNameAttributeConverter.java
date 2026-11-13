package com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa.converters;

import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ZoneName;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ZoneNameAttributeConverter implements AttributeConverter<ZoneName, String> {
    @Override
    public String convertToDatabaseColumn(ZoneName attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public ZoneName convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new ZoneName(dbData);
    }
}
