package com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa;

import com.famicare.FamicareBackend.zones.domain.model.aggregates.SafeZone;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ZoneName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for querying and persisting {@link SafeZone} aggregates.
 * - {@code existsByElderlyProfileIdAndName()} checks the unique zone name invariant on create.
 * - {@code existsByElderlyProfileIdAndNameAndIdNot()} checks it on update, ignoring the zone itself.
 * - {@code findAllByElderlyProfileId()} lists the zones to evaluate against a new position.
 */
@Repository
public interface SafeZoneRepository extends JpaRepository<SafeZone, Long> {

    boolean existsByElderlyProfileIdAndName(ElderlyProfileId elderlyProfileId, ZoneName name);

    boolean existsByElderlyProfileIdAndNameAndIdNot(ElderlyProfileId elderlyProfileId, ZoneName name, Long id);

    List<SafeZone> findAllByElderlyProfileId(ElderlyProfileId elderlyProfileId);
}
