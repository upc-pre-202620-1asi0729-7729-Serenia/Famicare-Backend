package com.famicare.FamicareBackend.monitor.interfaces.rest.transform;

import com.famicare.FamicareBackend.monitor.domain.model.aggregates.Alert;
import com.famicare.FamicareBackend.monitor.domain.model.commands.AttendAlertCommand;
import com.famicare.FamicareBackend.monitor.domain.model.commands.CreateAlertCommand;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.AlertCategory;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.AlertType;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.monitor.interfaces.rest.resources.AlertResource;
import com.famicare.FamicareBackend.monitor.interfaces.rest.resources.AttendAlertResource;
import com.famicare.FamicareBackend.monitor.interfaces.rest.resources.CreateAlertResource;

public class AlertAssembler {

    public static CreateAlertCommand toCommandFromResource(CreateAlertResource resource) {
        return new CreateAlertCommand(
                new ElderlyProfileId(resource.elderlyProfileId()),
                AlertType.valueOf(resource.type().toUpperCase()),
                AlertCategory.valueOf(resource.category().toUpperCase()),
                resource.title(),
                resource.description()
        );
    }

    public static AttendAlertCommand toCommandFromResource(Long alertId, AttendAlertResource resource) {
        return new AttendAlertCommand(
                alertId,
                resource != null ? resource.attendedBy() : "Valeria Ríos"
        );
    }

    public static AlertResource toResourceFromEntity(Alert entity) {
        return new AlertResource(
                entity.getId(),
                entity.getElderlyProfileId().elderlyProfileId(),
                entity.getType().name(),
                entity.getCategory().name(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus().name(),
                "",
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : "",
                ""
        );
    }
}