package com.famicare.FamicareBackend.monitor.application;

import com.famicare.FamicareBackend.monitor.domain.model.aggregates.LocationRecord;
import com.famicare.FamicareBackend.monitor.domain.model.aggregates.MonitorTelemetryEvent;
import com.famicare.FamicareBackend.monitor.domain.model.commands.TrackingCommands.*;
import com.famicare.FamicareBackend.monitor.domain.model.queries.TrackingQueries.*;

import java.util.List;
import java.util.Optional;

public interface TrackingService {
    LocationRecord handle(RecordLocationCommand command);
    MonitorTelemetryEvent handle(RecordTelemetryEventCommand command);
    Optional<LocationRecord> handle(GetLatestLocationQuery query);
    List<LocationRecord> handle(GetLocationHistoryQuery query);
    List<MonitorTelemetryEvent> handle(GetTelemetryEventsQuery query);
}