package com.famicare.FamicareBackend.monitor.domain.model.queries;

public interface TrackingQueries {
    record GetLatestLocationQuery(Long elderlyProfileId) {}
    record GetLocationHistoryQuery(Long elderlyProfileId) {}
    record GetTelemetryEventsQuery(Long elderlyProfileId) {}
}