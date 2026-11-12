package com.famicare.FamicareBackend.monitor.infrastructure.persistence.jpa;

import com.famicare.FamicareBackend.monitor.domain.model.aggregates.LocationRecord;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.ElderlyProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRecordRepository extends JpaRepository<LocationRecord, Long> {
    Optional<LocationRecord> findFirstByElderlyProfileIdOrderByRecordedAtDesc(ElderlyProfileId elderlyProfileId);
    List<LocationRecord> findByElderlyProfileIdOrderByRecordedAtDesc(ElderlyProfileId elderlyProfileId);
}