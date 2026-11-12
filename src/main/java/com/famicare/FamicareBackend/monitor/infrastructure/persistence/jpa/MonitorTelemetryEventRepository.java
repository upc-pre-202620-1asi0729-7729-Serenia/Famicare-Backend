package com.famicare.FamicareBackend.monitor.infrastructure.persistence.jpa;

import com.famicare.FamicareBackend.monitor.domain.model.aggregates.MonitorTelemetryEvent;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.ElderlyProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MonitorTelemetryEventRepository extends JpaRepository<MonitorTelemetryEvent, Long> {
    List<MonitorTelemetryEvent> findByElderlyProfileIdOrderByReportedAtDesc(ElderlyProfileId elderlyProfileId);
}