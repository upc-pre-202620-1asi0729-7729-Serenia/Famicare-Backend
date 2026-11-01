package com.famicare.FamicareBackend.iot.interfaces.rest.transform;

import com.famicare.FamicareBackend.iot.domain.model.aggregates.Device;
import com.famicare.FamicareBackend.iot.interfaces.rest.resources.DeviceResource;

import java.time.Instant;

/**
 * Interface layer translator converting a {@link Device} aggregate to a {@link DeviceResource}.
 */
public class DeviceResourceFromEntityAssembler {
    public static DeviceResource toResourceFromEntity(Device entity) {
        return new DeviceResource(
                entity.getId(),
                entity.getElderlyProfileId() == null ? null : entity.getElderlyProfileId().value(),
                entity.getPairingCode().value(),
                entity.getStatus().name(),
                entity.getBatteryLevel() == null ? null : entity.getBatteryLevel().value(),
                entity.getLastHeartbeat(),
                entity.getPairedAt(),
                entity.isOnline(Instant.now()));
    }
}
