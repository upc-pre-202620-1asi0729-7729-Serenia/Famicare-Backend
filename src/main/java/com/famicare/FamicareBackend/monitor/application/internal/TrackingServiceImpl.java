package com.famicare.FamicareBackend.monitor.application.internal;

import com.famicare.FamicareBackend.monitor.application.TrackingService;
import com.famicare.FamicareBackend.monitor.application.commandservices.AlertCommandService;
import com.famicare.FamicareBackend.monitor.domain.model.aggregates.LocationRecord;
import com.famicare.FamicareBackend.monitor.domain.model.aggregates.MonitorTelemetryEvent;
import com.famicare.FamicareBackend.monitor.domain.model.commands.CreateAlertCommand;
import com.famicare.FamicareBackend.monitor.domain.model.commands.TrackingCommands.*;
import com.famicare.FamicareBackend.monitor.domain.model.queries.TrackingQueries.*;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.AlertCategory;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.AlertType;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.monitor.infrastructure.persistence.jpa.LocationRecordRepository;
import com.famicare.FamicareBackend.monitor.infrastructure.persistence.jpa.MonitorTelemetryEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrackingServiceImpl implements TrackingService {

    private final LocationRecordRepository locationRecordRepository;
    private final MonitorTelemetryEventRepository telemetryEventRepository;
    private final AlertCommandService alertCommandService;

    public TrackingServiceImpl(LocationRecordRepository locationRecordRepository,
                               MonitorTelemetryEventRepository telemetryEventRepository,
                               AlertCommandService alertCommandService) {
        this.locationRecordRepository = locationRecordRepository;
        this.telemetryEventRepository = telemetryEventRepository;
        this.alertCommandService = alertCommandService;
    }

    @Override
    @Transactional
    public LocationRecord handle(RecordLocationCommand command) {
        return locationRecordRepository.save(new LocationRecord(
                new ElderlyProfileId(command.elderlyProfileId()),
                command.latitude(),
                command.longitude(),
                command.accuracy(),
                command.placeName()
        ));
    }

    @Override
    @Transactional
    public MonitorTelemetryEvent handle(RecordTelemetryEventCommand command) {
        MonitorTelemetryEvent savedEvent = telemetryEventRepository.save(new MonitorTelemetryEvent(
                new ElderlyProfileId(command.elderlyProfileId()),
                command.eventType(),
                command.details()
        ));

        switch (command.eventType()) {
            case PANIC -> alertCommandService.handle(new CreateAlertCommand(
                    new ElderlyProfileId(command.elderlyProfileId()),
                    AlertType.CRITICAL,
                    AlertCategory.SOS,
                    "Boton de panico accionado",
                    command.details() != null ? command.details() : "Se activó el boton de panico"
            ));
            case FALL -> alertCommandService.handle(new CreateAlertCommand(
                    new ElderlyProfileId(command.elderlyProfileId()),
                    AlertType.CRITICAL,
                    AlertCategory.FALL_DETECTED,
                    "Caida detectada",
                    command.details() != null ? command.details() : "Se ha detectado un impacto por caida"
            ));
            case INACTIVITY -> alertCommandService.handle(new CreateAlertCommand(
                    new ElderlyProfileId(command.elderlyProfileId()),
                    AlertType.WARNING,
                    AlertCategory.INACTIVITY,
                    "Inactividad inusual",
                    command.details() != null ? command.details() : "No se ha detectado movimiento por un tiempo prolongado"
            ));
            default -> {}
        }

        return savedEvent;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocationRecord> handle(GetLatestLocationQuery query) {
        return locationRecordRepository.findFirstByElderlyProfileIdOrderByRecordedAtDesc(new ElderlyProfileId(query.elderlyProfileId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationRecord> handle(GetLocationHistoryQuery query) {
        return locationRecordRepository.findByElderlyProfileIdOrderByRecordedAtDesc(new ElderlyProfileId(query.elderlyProfileId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitorTelemetryEvent> handle(GetTelemetryEventsQuery query) {
        return telemetryEventRepository.findByElderlyProfileIdOrderByReportedAtDesc(new ElderlyProfileId(query.elderlyProfileId()));
    }
}