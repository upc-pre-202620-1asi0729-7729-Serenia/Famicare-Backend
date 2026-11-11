package com.famicare.FamicareBackend.monitor.domain.model.events;

public record AlertAttendedEvent(Long alertId, String attendedBy) {

}