package com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa.converters;

import com.famicare.FamicareBackend.iot.domain.model.valueobjects.PairingCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PairingCodeAttributeConverter implements AttributeConverter<PairingCode, String> {
    @Override
    public String convertToDatabaseColumn(PairingCode attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public PairingCode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new PairingCode(dbData);
    }
}
