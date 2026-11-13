package com.famicare.FamicareBackend.care.application.internal.queryservices;

import com.famicare.FamicareBackend.care.application.queryservices.ElderlyProfileQueryService;
import com.famicare.FamicareBackend.care.domain.model.aggregates.ElderlyProfile;
import com.famicare.FamicareBackend.care.domain.model.queries.GetAllElderlyProfilesQuery;
import com.famicare.FamicareBackend.care.domain.model.queries.GetElderlyProfileByIdQuery;
import com.famicare.FamicareBackend.care.infrastructure.persistence.jpa.ElderlyProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ElderlyProfileQueryServiceImpl implements ElderlyProfileQueryService {

    private final ElderlyProfileRepository elderlyProfileRepository;

    public ElderlyProfileQueryServiceImpl(ElderlyProfileRepository elderlyProfileRepository) {
        this.elderlyProfileRepository = elderlyProfileRepository;
    }

    @Override
    public Optional<ElderlyProfile> handle(GetElderlyProfileByIdQuery query) {
        return elderlyProfileRepository.findById(query.elderlyProfileId());
    }

    @Override
    public List<ElderlyProfile> handle(GetAllElderlyProfilesQuery query) {
        return elderlyProfileRepository.findAll();
    }
}