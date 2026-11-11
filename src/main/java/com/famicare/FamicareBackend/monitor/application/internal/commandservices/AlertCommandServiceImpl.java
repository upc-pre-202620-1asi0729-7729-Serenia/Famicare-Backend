package com.famicare.FamicareBackend.monitor.application.internal.commandservices;

import com.famicare.FamicareBackend.monitor.application.commandservices.AlertCommandService;
import com.famicare.FamicareBackend.monitor.domain.model.aggregates.Alert;
import com.famicare.FamicareBackend.monitor.domain.model.commands.AttendAlertCommand;
import com.famicare.FamicareBackend.monitor.domain.model.commands.CreateAlertCommand;
import com.famicare.FamicareBackend.monitor.domain.model.commands.ResolveAlertCommand;
import com.famicare.FamicareBackend.monitor.infrastructure.persistence.jpa.AlertRepository;
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