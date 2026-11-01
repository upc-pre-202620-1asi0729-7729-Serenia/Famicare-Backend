package com.famicare.FamicareBackend.iot.interfaces.rest.transform;

import com.famicare.FamicareBackend.iot.domain.model.commands.RegisterDeviceCommand;
import com.famicare.FamicareBackend.iot.interfaces.rest.resources.RegisterDeviceResource;

/**
 * Interface layer translator converting a {@link RegisterDeviceResource} to a {@link RegisterDeviceCommand}.
 */
public class RegisterDeviceCommandFromResourceAssembler {
    public static RegisterDeviceCommand toCommandFromResource(RegisterDeviceResource resource) {
        return new RegisterDeviceCommand(
                IotValueObjectFromStringAssembler.toPairingCodeFromString(resource.pairingCode()));
    }
}
