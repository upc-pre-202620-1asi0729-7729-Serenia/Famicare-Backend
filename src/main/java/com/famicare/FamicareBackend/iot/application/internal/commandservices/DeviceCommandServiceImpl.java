package com.famicare.FamicareBackend.iot.application.internal.commandservices;

import com.famicare.FamicareBackend.iot.application.commandservices.DeviceCommandFailure;
import com.famicare.FamicareBackend.iot.application.commandservices.DeviceCommandService;
import com.famicare.FamicareBackend.iot.domain.model.aggregates.Device;
import com.famicare.FamicareBackend.iot.domain.model.commands.PairDeviceCommand;
import com.famicare.FamicareBackend.iot.domain.model.commands.RegisterDeviceCommand;
import com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa.DeviceRepository;
import com.famicare.FamicareBackend.shared.application.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.famicare.FamicareBackend.iot.domain.model.aggregates.Device.PAIRING_CODE_UNIQUE_CONSTRAINT;

@Slf4j
@Service
public class DeviceCommandServiceImpl implements DeviceCommandService {
    private static final String DUPLICATE_PAIRING_CODE_CONSTRAINT = PAIRING_CODE_UNIQUE_CONSTRAINT;
    private final DeviceRepository deviceRepository;

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * Deliberately not {@code @Transactional}: the repository call runs in its own transaction, so a
     * unique constraint violation can be caught here and turned into a failure Result without
     * leaving an outer transaction marked rollback-only.
     */
    @Override
    public Result<Device, DeviceCommandFailure> handle(RegisterDeviceCommand command) {
        if (deviceRepository.existsByPairingCode(command.pairingCode())) {
            log.warn("Duplicate pairing code detected (pre-check): pairingCode={}",
                    command.pairingCode().maskedForLogging());
            return Result.fallido(new DeviceCommandFailure.DuplicatePairingCode());
        }
        try {
            var device = new Device(command);
            var createdDevice = deviceRepository.saveAndFlush(device);
            log.info("Device registered: id={}, pairingCode={}",
                    createdDevice.getId(), command.pairingCode().maskedForLogging());
            return Result.logrado(createdDevice);
        } catch (DataIntegrityViolationException exception) {
            if (isDuplicatePairingCodeViolation(exception)) {
                log.warn("Duplicate pairing code detected (constraint violation): pairingCode={}",
                        command.pairingCode().maskedForLogging());
                return Result.fallido(new DeviceCommandFailure.DuplicatePairingCode());
            }
            log.error("Unexpected data integrity violation saving device: pairingCode={}",
                    command.pairingCode().maskedForLogging(), exception);
            throw exception;
        }
    }

    @Override
    @Transactional
    public Result<Device, DeviceCommandFailure> handle(PairDeviceCommand command) {
        var optionalDevice = deviceRepository.findByPairingCode(command.pairingCode());
        if (optionalDevice.isEmpty()) {
            log.warn("Pairing code not found: pairingCode={}", command.pairingCode().maskedForLogging());
            return Result.fallido(new DeviceCommandFailure.PairingCodeNotFound());
        }
        var device = optionalDevice.get();
        if (device.isPaired()) {
            log.warn("Device already paired: id={}", device.getId());
            return Result.fallido(new DeviceCommandFailure.AlreadyPaired());
        }
        device.pairTo(command.elderlyProfileId());
        var pairedDevice = deviceRepository.save(device);
        log.info("Device paired: id={}, elderlyProfileId={}",
                pairedDevice.getId(), command.elderlyProfileId().value());
        return Result.logrado(pairedDevice);
    }

    /**
     * Detect whether a data integrity violation corresponds to the pairing code unique constraint
     * @param exception persistence exception raised during save
     * @return {@code true} when the exception chain contains the duplicate constraint name
     */
    private boolean isDuplicatePairingCodeViolation(DataIntegrityViolationException exception) {
        Throwable violationCause = exception;
        while (violationCause != null) {
            String message = violationCause.getMessage();
            if (message != null && message.contains(DUPLICATE_PAIRING_CODE_CONSTRAINT)) return true;
            violationCause = violationCause.getCause();
        }
        return false;
    }
}
