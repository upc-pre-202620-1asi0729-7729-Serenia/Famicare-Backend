package com.famicare.FamicareBackend.alerts.infrastructure.persistence.jpa;

import com.famicare.FamicareBackend.alerts.domain.model.aggregates.Alert;
import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.AlertStatus;
import com.famicare.FamicareBackend.alerts.domain.model.valueobjects.ElderlyProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findAllByElderlyProfileIdOrderByCreatedAtDesc(ElderlyProfileId elderlyProfileId);
    List<Alert> findAllByElderlyProfileIdAndStatusOrderByCreatedAtDesc(ElderlyProfileId elderlyProfileId, AlertStatus status);
}