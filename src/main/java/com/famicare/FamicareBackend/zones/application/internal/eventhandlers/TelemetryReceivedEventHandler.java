package com.famicare.FamicareBackend.zones.application.internal.eventhandlers;

import com.famicare.FamicareBackend.iot.domain.model.events.TelemetryReceivedEvent;
import com.famicare.FamicareBackend.zones.application.commandservices.ZoneEvaluationCommandService;
import com.famicare.FamicareBackend.zones.domain.model.commands.EvaluateLocationCommand;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ElderlyProfileId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Reacts to the telemetry published by the IoT context to keep the zone evaluation continuous:
 * every message that carries a position is evaluated against the safe zones of its elderly profile.
 * The IoT types are translated to Zones value objects here, so the domain never depends on IoT.
 * It runs after the telemetry transaction commits, so a failure here never rejects the device's message.
 */
@Slf4j
@Component
public class TelemetryReceivedEventHandler {
    private final ZoneEvaluationCommandService zoneEvaluationCommandService;

    public TelemetryReceivedEventHandler(ZoneEvaluationCommandService zoneEvaluationCommandService) {
        this.zoneEvaluationCommandService = zoneEvaluationCommandService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(TelemetryReceivedEvent event) {
        if (event.coordinates() == null) return;
        try {
            var command = new EvaluateLocationCommand(
                    new ElderlyProfileId(event.elderlyProfileId().value()),
                    new Coordinates(event.coordinates().getLatitude(), event.coordinates().getLongitude()),
                    event.recordedAt());
            zoneEvaluationCommandService.handle(command);
        } catch (RuntimeException exception) {
            log.error("Zone evaluation failed: deviceId={}, elderlyProfileId={}",
                    event.deviceId(), event.elderlyProfileId().value(), exception);
        }
    }
}
