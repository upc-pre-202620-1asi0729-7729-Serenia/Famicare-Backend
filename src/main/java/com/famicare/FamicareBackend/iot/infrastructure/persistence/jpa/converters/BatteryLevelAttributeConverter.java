package com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa.converters;

import com.famicare.FamicareBackend.iot.domain.model.valueobjects.BatteryLevel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BatteryLevelAttributeConverter implements AttributeConverter<BatteryLevel, Integer> {
    @Override
    public Integer convertToDatabaseColumn(BatteryLevel attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public BatteryLevel convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : new BatteryLevel(dbData);
    }
}
