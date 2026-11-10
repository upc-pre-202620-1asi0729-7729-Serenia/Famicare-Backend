package com.famicare.FamicareBackend.alerts.domain.model.events;

public record AlertAttendedEvent(Long alertId, String attendedBy) {

}