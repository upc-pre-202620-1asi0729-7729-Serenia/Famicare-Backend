package com.famicare.FamicareBackend.care.application.queryservices;

import com.famicare.FamicareBackend.care.domain.model.aggregates.ElderlyProfile;
import com.famicare.FamicareBackend.care.domain.model.queries.GetAllElderlyProfilesQuery;
import com.famicare.FamicareBackend.care.domain.model.queries.GetElderlyProfileByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ElderlyProfileQueryService {
    Optional<ElderlyProfile> handle(GetElderlyProfileByIdQuery query);
    List<ElderlyProfile> handle(GetAllElderlyProfilesQuery query);
}