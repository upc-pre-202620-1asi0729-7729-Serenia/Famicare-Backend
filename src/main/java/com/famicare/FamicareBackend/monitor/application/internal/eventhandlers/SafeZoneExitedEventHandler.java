package com.famicare.FamicareBackend.monitor.application.internal.eventhandlers;

import com.famicare.FamicareBackend.monitor.application.commandservices.AlertCommandService;
import com.famicare.FamicareBackend.monitor.domain.model.commands.CreateAlertCommand;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.AlertCategory;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.AlertType;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.zones.domain.model.events.SafeZoneExitedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Reacts to the Zones context when an elderly person leaves a safe zone by raising a
 * {@link AlertCategory#ZONE_EXIT} alert for the care network (US-12).
 * It runs in the transaction that stored the crossing, so the zone presence and the alert
 * are saved together.
 */
@Component
public class SafeZoneExitedEventHandler {
    private final AlertCommandService alertCommandService;

    public SafeZoneExitedEventHandler(AlertCommandService alertCommandService) {
        this.alertCommandService = alertCommandService;
    }

    @EventListener
    public void on(SafeZoneExitedEvent event) {
        alertCommandService.handle(new CreateAlertCommand(
                new ElderlyProfileId(event.elderlyProfileId().value()),
                AlertType.WARNING,
                AlertCategory.ZONE_EXIT,
                "Salida de zona segura",
                "El adulto mayor salió de la zona segura \"" + event.zoneName() + "\""));
    }
}
