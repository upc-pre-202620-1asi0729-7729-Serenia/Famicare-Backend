package com.famicare.FamicareBackend.monitor.application.queryservices;

import com.famicare.FamicareBackend.monitor.domain.model.aggregates.Alert;
import com.famicare.FamicareBackend.monitor.domain.model.queries.GetAlertByIdQuery;
import com.famicare.FamicareBackend.monitor.domain.model.queries.GetAlertsByElderlyProfileIdAndStatusQuery;
import com.famicare.FamicareBackend.monitor.domain.model.queries.GetAlertsByElderlyProfileIdQuery;

import java.util.List;
import java.util.Optional;

public interface AlertQueryService {
    Optional<Alert> handle(GetAlertByIdQuery query);
    List<Alert> handle(GetAlertsByElderlyProfileIdQuery query);
    List<Alert> handle(GetAlertsByElderlyProfileIdAndStatusQuery query);
}