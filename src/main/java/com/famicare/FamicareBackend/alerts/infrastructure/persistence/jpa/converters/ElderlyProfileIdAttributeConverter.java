package com.famicare.FamicareBackend.alerts.infrastructure.persistence.jpa.converters;

import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.ElderlyProfileId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ElderlyProfileIdAttributeConverter implements AttributeConverter<ElderlyProfileId, Long> {

    @Override
    public Long convertToDatabaseColumn(ElderlyProfileId attribute) {
        return attribute != null ? attribute.elderlyProfileId() : null;
    }

    @Override
    public ElderlyProfileId convertToEntityAttribute(Long dbData) {
        return dbData != null ? new ElderlyProfileId(dbData) : null;
    }
}