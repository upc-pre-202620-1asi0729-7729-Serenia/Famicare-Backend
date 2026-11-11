package com.famicare.FamicareBackend.monitor.interfaces.rest.transform;

import com.famicare.FamicareBackend.monitor.domain.model.commands.AttendAlertCommand;
import com.famicare.FamicareBackend.monitor.interfaces.rest.resources.AttendAlertResource;

public class AttendAlertCommandFromResourceAssembler {
    public static AttendAlertCommand toCommandFromResource(Long alertId, AttendAlertResource resource) {
        return new AttendAlertCommand(alertId, resource.attendedBy());
    }
}