package com.famicare.FamicareBackend.alerts.interfaces.rest.transform;

import com.famicare.FamicareBackend.alerts.domain.model.commands.AttendAlertCommand;
import com.famicare.FamicareBackend.alerts.interfaces.rest.resources.AttendAlertResource;

public class AttendAlertCommandFromResourceAssembler {
    public static AttendAlertCommand toCommandFromResource(Long alertId, AttendAlertResource resource) {
        return new AttendAlertCommand(alertId, resource.attendedBy());
    }
}