package com.famicare.FamicareBackend.alerts.interfaces.rest.transform;

import com.famicare.FamicareBackend.alerts.domain.model.commands.CreateAlertCommand;
import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.AlertCategory;
import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.AlertType;
import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.alerts.interfaces.rest.resources.CreateAlertResource;

public class CreateAlertCommandFromResourceAssembler {
    public static CreateAlertCommand toCommandFromResource(CreateAlertResource resource) {
        return new CreateAlertCommand(
                new ElderlyProfileId(resource.elderlyProfileId()),
                AlertType.valueOf(resource.type().toUpperCase()),
                AlertCategory.valueOf(resource.category().toUpperCase()),
                resource.title(),
                resource.description()
        );
    }
}