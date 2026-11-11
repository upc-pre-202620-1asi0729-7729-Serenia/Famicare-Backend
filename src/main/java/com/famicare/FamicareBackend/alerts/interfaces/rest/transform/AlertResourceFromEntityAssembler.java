package com.famicare.FamicareBackend.alerts.interfaces.rest.transform;

import com.famicare.FamicareBackend.alerts.domain.model.aggregates.Alert;
import com.famicare.FamicareBackend.alerts.interfaces.rest.resources.AlertResource;

public class AlertResourceFromEntityAssembler {
    public static AlertResource toResourceFromEntity(Alert entity) {
        return new AlertResource(
                entity.getId(),
                entity.getElderlyProfileId().elderlyProfileId(),
                entity.getType().name(),
                entity.getCategory().name(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus().name(),
                entity.getAttendedBy(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
                entity.getResolvedAt() != null ? entity.getResolvedAt().toString() : null
        );
    }
}