package com.famicare.FamicareBackend.iot.interfaces.rest.transform;

import com.famicare.FamicareBackend.iot.domain.model.commands.PairDeviceCommand;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.iot.interfaces.rest.resources.PairDeviceResource;

/**
 * Interface layer translator converting a {@link PairDeviceResource} to a {@link PairDeviceCommand}.
 */
public class PairDeviceCommandFromResourceAssembler {
    public static PairDeviceCommand toCommandFromResource(PairDeviceResource resource) {
        return new PairDeviceCommand(
                IotValueObjectFromStringAssembler.toPairingCodeFromString(resource.pairingCode()),
                new ElderlyProfileId(resource.elderlyProfileId()));
    }
}
