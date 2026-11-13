package com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa;

import com.famicare.FamicareBackend.zones.domain.model.aggregates.LastKnownLocation;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ElderlyProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for querying and persisting {@link LastKnownLocation} aggregates.
 */
@Repository
public interface LastKnownLocationRepository extends JpaRepository<LastKnownLocation, Long> {

    Optional<LastKnownLocation> findByElderlyProfileId(ElderlyProfileId elderlyProfileId);
}
