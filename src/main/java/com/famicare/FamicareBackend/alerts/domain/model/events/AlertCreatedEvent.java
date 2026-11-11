package com.famicare.FamicareBackend.alerts.domain.model.events;

import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.AlertCategory;
import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.AlertType;
import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.ElderlyProfileId;

public record AlertCreatedEvent(Long alertId, ElderlyProfileId elderlyProfileId, AlertType type, AlertCategory category) {

}