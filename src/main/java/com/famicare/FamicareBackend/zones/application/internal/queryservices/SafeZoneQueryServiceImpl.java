package com.famicare.FamicareBackend.zones.application.internal.queryservices;

import com.famicare.FamicareBackend.zones.application.queryservices.SafeZoneQueryService;
import com.famicare.FamicareBackend.zones.domain.model.aggregates.SafeZone;
import com.famicare.FamicareBackend.zones.domain.model.queries.GetSafeZoneByIdQuery;
import com.famicare.FamicareBackend.zones.domain.model.queries.GetSafeZonesByElderlyProfileIdQuery;
import com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa.SafeZoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class SafeZoneQueryServiceImpl implements SafeZoneQueryService {

    private final SafeZoneRepository safeZoneRepository;

    public SafeZoneQueryServiceImpl(SafeZoneRepository safeZoneRepository) {
        this.safeZoneRepository = safeZoneRepository;
    }

    @Override
    public Optional<SafeZone> handle(GetSafeZoneByIdQuery query) {
        var result = safeZoneRepository.findById(query.safeZoneId());
        if (result.isEmpty()) log.debug("No safe zone found for id={}", query.safeZoneId());
        return result;
    }

    @Override
    public List<SafeZone> handle(GetSafeZonesByElderlyProfileIdQuery query) {
        return safeZoneRepository.findAllByElderlyProfileId(query.elderlyProfileId());
    }
}
