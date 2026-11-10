package com.famicare.FamicareBackend.alerts.application.commandservices;

import com.famicare.FamicareBackend.alerts.domain.model.aggregates.Alert;
import com.famicare.FamicareBackend.alerts.domain.model.commands.AttendAlertCommand;
import com.famicare.FamicareBackend.alerts.domain.model.commands.CreateAlertCommand;
import com.famicare.FamicareBackend.alerts.domain.model.commands.ResolveAlertCommand;

import java.util.Optional;

public interface AlertCommandService {
    Optional<Alert> handle(CreateAlertCommand command);
    Optional<Alert> handle(AttendAlertCommand command);
    Optional<Alert> handle(ResolveAlertCommand command);
}