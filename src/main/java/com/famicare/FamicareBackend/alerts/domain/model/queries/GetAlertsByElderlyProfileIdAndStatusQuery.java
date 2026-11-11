package com.famicare.FamicareBackend.alerts.domain.model.queries;

import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.AlertStatus;
import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.ElderlyProfileId;

public record GetAlertsByElderlyProfileIdAndStatusQuery(ElderlyProfileId elderlyProfileId, AlertStatus status) {

}