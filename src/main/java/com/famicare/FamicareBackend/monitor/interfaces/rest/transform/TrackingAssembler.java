package com.famicare.FamicareBackend.monitor.interfaces.rest.transform;

import com.famicare.FamicareBackend.monitor.domain.model.aggregates.LocationRecord;
import com.famicare.FamicareBackend.monitor.domain.model.aggregates.MonitorTelemetryEvent;
import com.famicare.FamicareBackend.monitor.domain.model.commands.TrackingCommands.*;
import com.famicare.FamicareBackend.monitor.interfaces.rest.resources.TrackingResources.*;

public class TrackingAssembler {

    public static RecordLocationCommand toCommand(RecordLocationResource r) {
        return new RecordLocationCommand(r.elderlyProfileId(), r.latitude(), r.longitude(), r.accuracy(), r.placeName());
    }

    public static LocationRecordResource toResource(LocationRecord e) {
        return new LocationRecordResource(e.getId(), e.getElderlyProfileId().elderlyProfileId(), e.getLatitude(), e.getLongitude(), e.getAccuracy(), e.getPlaceName(), e.getRecordedAt());
    }

    public static RecordTelemetryEventCommand toCommand(RecordTelemetryEventResource r) {
        return new RecordTelemetryEventCommand(r.elderlyProfileId(), r.eventType(), r.details());
    }

    public static TelemetryEventResource toResource(MonitorTelemetryEvent e) {
        return new TelemetryEventResource(e.getId(), e.getElderlyProfileId().elderlyProfileId(), e.getEventType().name(), e.getDetails(), e.getReportedAt());
    }
}