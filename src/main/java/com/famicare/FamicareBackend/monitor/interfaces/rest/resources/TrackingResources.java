package com.famicare.FamicareBackend.monitor.interfaces.rest.resources;

import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.TelemetryEventType;
import java.time.LocalDateTime;

public interface TrackingResources {
    record RecordLocationResource(Long elderlyProfileId, Double latitude, Double longitude, Double accuracy, String placeName) {}
    record LocationRecordResource(Long id, Long elderlyProfileId, Double latitude, Double longitude, Double accuracy, String placeName, LocalDateTime recordedAt) {}
    record RecordTelemetryEventResource(Long elderlyProfileId, TelemetryEventType eventType, String details) {}
    record TelemetryEventResource(Long id, Long elderlyProfileId, String eventType, String details, LocalDateTime reportedAt) {}
}