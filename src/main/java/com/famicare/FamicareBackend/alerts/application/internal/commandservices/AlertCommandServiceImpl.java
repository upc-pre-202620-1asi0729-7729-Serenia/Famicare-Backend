package com.famicare.FamicareBackend.alerts.application.internal.commandservices;

import com.famicare.FamicareBackend.alerts.application.commandservices.AlertCommandService;
import com.famicare.FamicareBackend.alerts.domain.model.aggregates.Alert;
import com.famicare.FamicareBackend.alerts.domain.model.commands.AttendAlertCommand;
import com.famicare.FamicareBackend.alerts.domain.model.commands.CreateAlertCommand;
import com.famicare.FamicareBackend.alerts.domain.model.commands.ResolveAlertCommand;
import com.famicare.FamicareBackend.alerts.infrastructure.persistence.jpa.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AlertCommandServiceImpl implements AlertCommandService {

    private final AlertRepository alertRepository;

    public AlertCommandServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public Optional<Alert> handle(CreateAlertCommand command) {
        var alert = new Alert(command);
        var createdAlert = alertRepository.save(alert);
        return Optional.of(createdAlert);
    }

    @Override
    public Optional<Alert> handle(AttendAlertCommand command) {
        return alertRepository.findById(command.alertId()).map(alert -> {
            alert.markAsAttended(command.attendedBy());
            return alertRepository.save(alert);
        });
    }

    @Override
    public Optional<Alert> handle(ResolveAlertCommand command) {
        return alertRepository.findById(command.alertId()).map(alert -> {
            alert.markAsResolved();
            return alertRepository.save(alert);
        });
    }
}