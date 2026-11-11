package com.famicare.FamicareBackend.monitor.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ElderlyProfileId(Long elderlyProfileId) {
    public ElderlyProfileId {
        if (elderlyProfileId == null || elderlyProfileId <= 0) {
            throw new IllegalArgumentException("Must be a valid positive identifier");
        }
    }
}