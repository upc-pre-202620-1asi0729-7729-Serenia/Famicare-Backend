package com.famicare.FamicareBackend.iot.application.commandservices;

import com.famicare.FamicareBackend.iot.domain.model.aggregates.TelemetryEvent;
import com.famicare.FamicareBackend.iot.domain.model.commands.RecordTelemetryCommand;
import com.famicare.FamicareBackend.shared.application.result.Result;

/**
 * Application service contract for telemetry command operations.
 */
public interface TelemetryCommandService {

    /**
     * Records a message sent by a paired device, refreshes the device heartbeat and
     * battery level, and publishes a {@code TelemetryReceivedEvent}.
     *
     * @param command telemetry data
     * @return success with the stored event, failure when the device does not exist or is not paired
     */
    Result<TelemetryEvent, TelemetryCommandFailure> handle(RecordTelemetryCommand command);
}
