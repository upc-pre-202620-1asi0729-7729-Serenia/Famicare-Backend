package com.famicare.FamicareBackend.zones.domain.model.queries;

public record GetSafeZoneByIdQuery(Long safeZoneId) {
    public GetSafeZoneByIdQuery {
        if (safeZoneId == null || safeZoneId <= 0) {
            throw new IllegalArgumentException("zone.error.id.invalid");
        }
    }
}
