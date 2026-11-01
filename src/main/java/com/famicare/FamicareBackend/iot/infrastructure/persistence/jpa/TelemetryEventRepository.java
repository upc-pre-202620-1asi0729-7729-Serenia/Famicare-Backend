package com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa;

import com.famicare.FamicareBackend.iot.domain.model.aggregates.TelemetryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Repository for querying and persisting {@link TelemetryEvent} aggregates.
 */
@Repository
public interface TelemetryEventRepository extends JpaRepository<TelemetryEvent, Long> {

    /**
     * Finds the events of a device recorded in the given period (both bounds inclusive),
     * oldest first.
     */
    List<TelemetryEvent> findAllByDeviceIdAndRecordedAtBetweenOrderByRecordedAtAsc(
            Long deviceId, Instant from, Instant to);
}
