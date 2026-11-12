package com.famicare.FamicareBackend.zones.domain.model.queries;

import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ElderlyProfileId;

/**
 * Safe zones that belong to an elderly profile.
 */
public record GetSafeZonesByElderlyProfileIdQuery(ElderlyProfileId elderlyProfileId) {
    public GetSafeZonesByElderlyProfileIdQuery {
        if (elderlyProfileId == null)
            throw new IllegalArgumentException("zone.error.elderlyProfileId.invalid");
    }
}
