package com.famicare.FamicareBackend.zones.application.internal.commandservices;

import com.famicare.FamicareBackend.shared.application.result.Result;
import com.famicare.FamicareBackend.zones.application.commandservices.SafeZoneCommandFailure;
import com.famicare.FamicareBackend.zones.application.commandservices.SafeZoneCommandService;
import com.famicare.FamicareBackend.zones.domain.model.aggregates.SafeZone;
import com.famicare.FamicareBackend.zones.domain.model.commands.CreateSafeZoneCommand;
import com.famicare.FamicareBackend.zones.domain.model.commands.UpdateSafeZoneCommand;
import com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa.LastKnownLocationRepository;
import com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa.SafeZoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static com.famicare.FamicareBackend.zones.domain.model.aggregates.SafeZone.NAME_UNIQUE_CONSTRAINT;

@Slf4j
@Service
public class SafeZoneCommandServiceImpl implements SafeZoneCommandService {
    private static final String DUPLICATE_NAME_CONSTRAINT = NAME_UNIQUE_CONSTRAINT;
    private final SafeZoneRepository safeZoneRepository;
    private final LastKnownLocationRepository lastKnownLocationRepository;

    public SafeZoneCommandServiceImpl(SafeZoneRepository safeZoneRepository,
                                      LastKnownLocationRepository lastKnownLocationRepository) {
        this.safeZoneRepository = safeZoneRepository;
        this.lastKnownLocationRepository = lastKnownLocationRepository;
    }

    /**
     * Deliberately not {@code @Transactional}: the repository call runs in its own transaction, so a
     * unique constraint violation can be caught here and turned into a failure Result without
     * leaving an outer transaction marked rollback-only.
     */
    @Override
    public Result<SafeZone, SafeZoneCommandFailure> handle(CreateSafeZoneCommand command) {
        if (safeZoneRepository.existsByElderlyProfileIdAndName(command.elderlyProfileId(), command.name())) {
            log.warn("Duplicate zone name detected (pre-check): elderlyProfileId={}, name={}",
                    command.elderlyProfileId().value(), command.name().value());
            return Result.fallido(new SafeZoneCommandFailure.DuplicateZoneName());
        }
        try {
            var safeZone = new SafeZone(command, Instant.now());
            lastKnownLocationRepository.findByElderlyProfileId(command.elderlyProfileId())
                    .ifPresent(location -> safeZone.syncPresence(location.getCoordinates(), location.getRecordedAt()));
            var createdZone = safeZoneRepository.saveAndFlush(safeZone);
            log.info("Safe zone created: id={}, elderlyProfileId={}, radius={}m",
                    createdZone.getId(), command.elderlyProfileId().value(), command.radius().value());
            return Result.logrado(createdZone);
        } catch (DataIntegrityViolationException exception) {
            if (isDuplicateNameViolation(exception)) {
                log.warn("Duplicate zone name detected (constraint violation): elderlyProfileId={}, name={}",
                        command.elderlyProfileId().value(), command.name().value());
                return Result.fallido(new SafeZoneCommandFailure.DuplicateZoneName());
            }
            log.error("Unexpected data integrity violation saving safe zone: elderlyProfileId={}",
                    command.elderlyProfileId().value(), exception);
            throw exception;
        }
    }

    @Override
    @Transactional
    public Result<SafeZone, SafeZoneCommandFailure> handle(UpdateSafeZoneCommand command) {
        var optionalZone = safeZoneRepository.findById(command.safeZoneId());
        if (optionalZone.isEmpty()) {
            log.warn("Safe zone not found: id={}", command.safeZoneId());
            return Result.fallido(new SafeZoneCommandFailure.SafeZoneNotFound());
        }
        var safeZone = optionalZone.get();
        if (safeZoneRepository.existsByElderlyProfileIdAndNameAndIdNot(
                safeZone.getElderlyProfileId(), command.name(), safeZone.getId())) {
            log.warn("Duplicate zone name detected on update: id={}, name={}",
                    safeZone.getId(), command.name().value());
            return Result.fallido(new SafeZoneCommandFailure.DuplicateZoneName());
        }
        safeZone.update(command, Instant.now());
        lastKnownLocationRepository.findByElderlyProfileId(safeZone.getElderlyProfileId())
                .ifPresent(location -> safeZone.syncPresence(location.getCoordinates(), location.getRecordedAt()));
        var updatedZone = safeZoneRepository.save(safeZone);
        log.info("Safe zone updated: id={}", updatedZone.getId());
        return Result.logrado(updatedZone);
    }

    /**
     * Detect whether a data integrity violation corresponds to the zone name unique constraint
     * @param exception persistence exception raised during save
     * @return {@code true} when the exception chain contains the duplicate constraint name
     */
    private boolean isDuplicateNameViolation(DataIntegrityViolationException exception) {
        Throwable violationCause = exception;
        while (violationCause != null) {
            String message = violationCause.getMessage();
            if (message != null && message.contains(DUPLICATE_NAME_CONSTRAINT)) return true;
            violationCause = violationCause.getCause();
        }
        return false;
    }
}
