package com.famicare.FamicareBackend.monitor.application.internal.queryservices;

import com.famicare.FamicareBackend.monitor.application.queryservices.AlertQueryService;
import com.famicare.FamicareBackend.monitor.domain.model.aggregates.Alert;
import com.famicare.FamicareBackend.monitor.domain.model.queries.GetAlertByIdQuery;
import com.famicare.FamicareBackend.monitor.domain.model.queries.GetAlertsByElderlyProfileIdAndStatusQuery;
import com.famicare.FamicareBackend.monitor.domain.model.queries.GetAlertsByElderlyProfileIdQuery;
import com.famicare.FamicareBackend.monitor.infrastructure.persistence.jpa.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AlertQueryServiceImpl implements AlertQueryService {

    private final AlertRepository alertRepository;

    public AlertQueryServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public Optional<Alert> handle(GetAlertByIdQuery query) {
        return alertRepository.findById(query.alertId());
    }

    @Override
    public List<Alert> handle(GetAlertsByElderlyProfileIdQuery query) {
        return alertRepository.findAllByElderlyProfileIdOrderByCreatedAtDesc(query.elderlyProfileId());
    }

    @Override
    public List<Alert> handle(GetAlertsByElderlyProfileIdAndStatusQuery query) {
        return alertRepository.findAllByElderlyProfileIdAndStatusOrderByCreatedAtDesc(
                query.elderlyProfileId(), query.status()
        );
    }
}