package com.famicare.FamicareBackend.care.interfaces.rest.transform;

import com.famicare.FamicareBackend.care.domain.model.aggregates.Caregiver;
import com.famicare.FamicareBackend.care.interfaces.rest.resources.CaregiverResource;

public class CaregiverResourceFromEntityAssembler {
    public static CaregiverResource toResourceFromEntity(Caregiver entity) {
        return new CaregiverResource(
                entity.getId(),
                entity.getElderlyProfileId(),
                entity.getInitials(),
                entity.getFullName(),
                entity.getEmail().value(),
                entity.getRole().name(),
                entity.getRelationshipLabel(),
                entity.getAvailability(),
                entity.getSchedule(),
                entity.getStatus().name(),
                entity.getInvitedAt()
        );
    }
}