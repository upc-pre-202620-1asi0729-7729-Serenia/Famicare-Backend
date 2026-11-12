package com.famicare.FamicareBackend.monitor.domain.model.commands;

import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.TelemetryEventType;

public interface TrackingCommands {
    record RecordLocationCommand(Long elderlyProfileId, Double latitude, Double longitude, Double accuracy, String placeName) {}
    record RecordTelemetryEventCommand(Long elderlyProfileId, TelemetryEventType eventType, String details) {}
}