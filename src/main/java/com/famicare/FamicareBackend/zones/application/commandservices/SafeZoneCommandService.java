package com.famicare.FamicareBackend.zones.application.commandservices;

import com.famicare.FamicareBackend.shared.application.result.Result;
import com.famicare.FamicareBackend.zones.domain.model.aggregates.SafeZone;
import com.famicare.FamicareBackend.zones.domain.model.commands.CreateSafeZoneCommand;
import com.famicare.FamicareBackend.zones.domain.model.commands.UpdateSafeZoneCommand;

/**
 * Application service contract for safe zone command operations.
 * Commands return a Result that distinguishes success from the business failures
 * described in {@link SafeZoneCommandFailure}.
 */
public interface SafeZoneCommandService {

    /**
     * Defines a new safe zone (US-10). Its presence starts from the last known coordinate of
     * the elderly profile, if there is one, without raising any event.
     *
     * @param command zone definition
     * @return success with the created zone, failure when the name is already used by the profile
     */
    Result<SafeZone, SafeZoneCommandFailure> handle(CreateSafeZoneCommand command);

    /**
     * Replaces the name and area of a safe zone (US-11) and re-synchronizes its presence with the
     * last known coordinate, without raising any event.
     *
     * @param command new name and area
     * @return success with the updated zone, failure when the zone does not exist or the name is already used
     */
    Result<SafeZone, SafeZoneCommandFailure> handle(UpdateSafeZoneCommand command);
}
