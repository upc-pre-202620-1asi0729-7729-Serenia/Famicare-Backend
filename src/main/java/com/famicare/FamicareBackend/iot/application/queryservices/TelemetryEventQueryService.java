package com.famicare.FamicareBackend.iot.application.queryservices;

import com.famicare.FamicareBackend.iot.domain.model.aggregates.TelemetryEvent;
import com.famicare.FamicareBackend.iot.domain.model.queries.GetTelemetryEventsByDeviceIdAndPeriodQuery;

import java.util.List;

public interface TelemetryEventQueryService {
    List<TelemetryEvent> handle(GetTelemetryEventsByDeviceIdAndPeriodQuery query);
}
