package com.famicare.FamicareBackend.zones.application.queryservices;

import com.famicare.FamicareBackend.zones.domain.model.aggregates.SafeZone;
import com.famicare.FamicareBackend.zones.domain.model.queries.GetSafeZoneByIdQuery;
import com.famicare.FamicareBackend.zones.domain.model.queries.GetSafeZonesByElderlyProfileIdQuery;

import java.util.List;
import java.util.Optional;

public interface SafeZoneQueryService {
    Optional<SafeZone> handle(GetSafeZoneByIdQuery query);
    List<SafeZone> handle(GetSafeZonesByElderlyProfileIdQuery query);
}
