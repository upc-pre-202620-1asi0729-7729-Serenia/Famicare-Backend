package com.famicare.FamicareBackend.care.application.queryservices;

import com.famicare.FamicareBackend.care.domain.model.aggregates.Caregiver;
import com.famicare.FamicareBackend.care.domain.model.queries.GetCaregiverByIdQuery;
import com.famicare.FamicareBackend.care.domain.model.queries.GetCaregiversByElderlyProfileIdQuery;

import java.util.List;
import java.util.Optional;

public interface CaregiverQueryService {
    List<Caregiver> handle(GetCaregiversByElderlyProfileIdQuery query);
    Optional<Caregiver> handle(GetCaregiverByIdQuery query);
}