package com.famicare.FamicareBackend.care.interfaces.rest.transform;

import com.famicare.FamicareBackend.care.domain.model.aggregates.ElderlyProfile;
import com.famicare.FamicareBackend.care.interfaces.rest.resources.ElderlyProfileResource;

public class ElderlyProfileResourceFromEntityAssembler {
    public static ElderlyProfileResource toResourceFromEntity(ElderlyProfile entity) {
        return new ElderlyProfileResource(
                entity.getId(),
                entity.getFullName(),
                entity.getAge(),
                entity.getMedicalNotes(),
                entity.getCreatedAt()
        );
    }
}