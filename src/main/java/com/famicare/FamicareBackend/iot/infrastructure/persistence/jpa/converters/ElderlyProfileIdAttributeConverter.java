package com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa.converters;

import com.famicare.FamicareBackend.iot.domain.model.valueobjects.ElderlyProfileId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ElderlyProfileIdAttributeConverter implements AttributeConverter<ElderlyProfileId, Long> {
    @Override
    public Long convertToDatabaseColumn(ElderlyProfileId attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public ElderlyProfileId convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new ElderlyProfileId(dbData);
    }
}
