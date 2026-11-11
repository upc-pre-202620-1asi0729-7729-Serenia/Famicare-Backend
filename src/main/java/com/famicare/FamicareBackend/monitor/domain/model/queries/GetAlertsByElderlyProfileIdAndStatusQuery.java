package com.famicare.FamicareBackend.monitor.domain.model.queries;

import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.AlertStatus;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.ElderlyProfileId;

public record GetAlertsByElderlyProfileIdAndStatusQuery(ElderlyProfileId elderlyProfileId, AlertStatus status) {

}