package com.famicare.FamicareBackend.iot.application.commandservices;

import com.famicare.FamicareBackend.iot.domain.model.aggregates.Device;
import com.famicare.FamicareBackend.iot.domain.model.commands.PairDeviceCommand;
import com.famicare.FamicareBackend.iot.domain.model.commands.RegisterDeviceCommand;
import com.famicare.FamicareBackend.shared.application.result.Result;

/**
 * Application service contract for device command operations.
 * Commands return a Result that distinguishes success from the business failures
 * described in {@link DeviceCommandFailure}.
 */
public interface DeviceCommandService {

    /**
     * Registers a new device in {@code PENDING} status.
     *
     * @param command command containing the pairing code
     * @return success with the created device, failure when the pairing code is already registered
     */
    Result<Device, DeviceCommandFailure> handle(RegisterDeviceCommand command);

    /**
     * Links a registered device to an elderly profile (US-07).
     *
     * @param command command containing the pairing code and the elderly profile id
     * @return success with the paired device, failure when the code is unknown or the device is already paired
     */
    Result<Device, DeviceCommandFailure> handle(PairDeviceCommand command);
}
