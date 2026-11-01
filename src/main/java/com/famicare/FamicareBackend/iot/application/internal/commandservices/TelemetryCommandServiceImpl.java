package com.famicare.FamicareBackend.iot.application.internal.commandservices;

import com.famicare.FamicareBackend.iot.application.commandservices.TelemetryCommandFailure;
import com.famicare.FamicareBackend.iot.application.commandservices.TelemetryCommandService;
import com.famicare.FamicareBackend.iot.domain.model.aggregates.TelemetryEvent;
import com.famicare.FamicareBackend.iot.domain.model.commands.RecordTelemetryCommand;
import com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa.DeviceRepository;
import com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa.TelemetryEventRepository;
import com.famicare.FamicareBackend.shared.application.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
public class TelemetryCommandServiceImpl implements TelemetryCommandService {
    private final DeviceRepository deviceRepository;
    private final TelemetryEventRepository telemetryEventRepository;

    public TelemetryCommandServiceImpl(DeviceRepository deviceRepository,
                                       TelemetryEventRepository telemetryEventRepository) {
        this.deviceRepository = deviceRepository;
        this.telemetryEventRepository = telemetryEventRepository;
    }

    @Override
    @Transactional
    public Result<TelemetryEvent, TelemetryCommandFailure> handle(RecordTelemetryCommand command) {
        var optionalDevice = deviceRepository.findById(command.deviceId());
        if (optionalDevice.isEmpty()) {
            log.warn("Telemetry rejected, device not found: deviceId={}", command.deviceId());
            return Result.fallido(new TelemetryCommandFailure.DeviceNotFound());
        }
        var device = optionalDevice.get();
        if (!device.isPaired()) {
            log.warn("Telemetry rejected, device not paired: deviceId={}", command.deviceId());
            return Result.fallido(new TelemetryCommandFailure.DeviceNotPaired());
        }
        device.registerHeartbeat(command.batteryLevel(), Instant.now());
        deviceRepository.save(device);

        var telemetryEvent = new TelemetryEvent(device, command);
        var storedEvent = telemetryEventRepository.save(telemetryEvent);
        log.debug("Telemetry recorded: id={}, deviceId={}, type={}",
                storedEvent.getId(), storedEvent.getDeviceId(), storedEvent.getEventType());
        return Result.logrado(storedEvent);
    }
}
