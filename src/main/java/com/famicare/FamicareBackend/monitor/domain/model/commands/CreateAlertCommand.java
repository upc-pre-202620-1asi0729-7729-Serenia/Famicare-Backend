package com.famicare.FamicareBackend.monitor.domain.model.commands;

import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.AlertCategory;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.AlertType;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.ElderlyProfileId;

public record CreateAlertCommand(ElderlyProfileId elderlyProfileId, AlertType type, AlertCategory category, String title, String description) {

}