package com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa;

import com.famicare.FamicareBackend.iot.domain.model.aggregates.Device;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.PairingCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for querying and persisting {@link Device} aggregates.
 * - {@code existsByPairingCode()} checks the unique pairing code invariant.
 * - {@code findByPairingCode()} locates the device a caregiver wants to pair.
 * - {@code findAllByElderlyProfileId()} lists the devices linked to an elderly profile.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    boolean existsByPairingCode(PairingCode pairingCode);

    Optional<Device> findByPairingCode(PairingCode pairingCode);

    List<Device> findAllByElderlyProfileId(ElderlyProfileId elderlyProfileId);
}
