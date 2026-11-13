package com.famicare.FamicareBackend.zones.application.internal.commandservices;

import com.famicare.FamicareBackend.zones.application.commandservices.ZoneEvaluationCommandService;
import com.famicare.FamicareBackend.zones.domain.model.aggregates.LastKnownLocation;
import com.famicare.FamicareBackend.zones.domain.model.commands.EvaluateLocationCommand;
import com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa.LastKnownLocationRepository;
import com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa.SafeZoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class ZoneEvaluationCommandServiceImpl implements ZoneEvaluationCommandService {
    private final LastKnownLocationRepository lastKnownLocationRepository;
    private final SafeZoneRepository safeZoneRepository;

    public ZoneEvaluationCommandServiceImpl(LastKnownLocationRepository lastKnownLocationRepository,
                                            SafeZoneRepository safeZoneRepository) {
        this.lastKnownLocationRepository = lastKnownLocationRepository;
        this.safeZoneRepository = safeZoneRepository;
    }

    /**
     * Runs in its own transaction because it is triggered after the telemetry transaction has
     * committed. The new last known location, the zone presences and the events raised by the
     * crossings are stored together or not at all.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<LastKnownLocation> handle(EvaluateLocationCommand command) {
        var optionalLocation = lastKnownLocationRepository.findByElderlyProfileId(command.elderlyProfileId());
        LastKnownLocation lastKnownLocation;
        if (optionalLocation.isEmpty()) {
            lastKnownLocation = new LastKnownLocation(command);
        } else {
            lastKnownLocation = optionalLocation.get();
            if (!lastKnownLocation.moveTo(command.coordinates(), command.recordedAt())) {
                log.debug("Stale location discarded: elderlyProfileId={}, recordedAt={}",
                        command.elderlyProfileId().value(), command.recordedAt());
                return Optional.empty();
            }
        }
        var storedLocation = lastKnownLocationRepository.save(lastKnownLocation);

        var safeZones = safeZoneRepository.findAllByElderlyProfileId(command.elderlyProfileId());
        safeZones.forEach(zone -> zone.evaluate(command.coordinates(), command.recordedAt()));
        safeZoneRepository.saveAll(safeZones);
        log.debug("Location evaluated: elderlyProfileId={}, zones={}",
                command.elderlyProfileId().value(), safeZones.size());
        return Optional.of(storedLocation);
    }
}
