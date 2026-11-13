package com.famicare.FamicareBackend.care.application.internal.queryservices;

import com.famicare.FamicareBackend.care.application.queryservices.CaregiverQueryService;
import com.famicare.FamicareBackend.care.domain.model.aggregates.Caregiver;
import com.famicare.FamicareBackend.care.domain.model.queries.GetCaregiverByIdQuery;
import com.famicare.FamicareBackend.care.domain.model.queries.GetCaregiversByElderlyProfileIdQuery;
import com.famicare.FamicareBackend.care.domain.model.valueobjects.InvitationStatus;
import com.famicare.FamicareBackend.care.infrastructure.persistence.jpa.CaregiverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaregiverQueryServiceImpl implements CaregiverQueryService {

    private final CaregiverRepository caregiverRepository;

    public CaregiverQueryServiceImpl(CaregiverRepository caregiverRepository) {
        this.caregiverRepository = caregiverRepository;
    }

    @Override
    public List<Caregiver> handle(GetCaregiversByElderlyProfileIdQuery query) {
        return caregiverRepository.findByElderlyProfileIdAndStatusNot(
                query.elderlyProfileId(), InvitationStatus.REVOKED);
    }

    @Override
    public Optional<Caregiver> handle(GetCaregiverByIdQuery query) {
        return caregiverRepository.findById(query.caregiverId());
    }
}