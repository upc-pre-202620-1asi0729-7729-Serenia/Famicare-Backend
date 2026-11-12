package com.famicare.FamicareBackend.zones.application.commandservices;

import com.famicare.FamicareBackend.zones.domain.model.aggregates.LastKnownLocation;
import com.famicare.FamicareBackend.zones.domain.model.commands.EvaluateLocationCommand;

import java.util.Optional;

/**
 * Application service contract that keeps the last known coordinate of an elderly profile and
 * evaluates it against the boundaries of their safe zones (US-12).
 */
public interface ZoneEvaluationCommandService {

    /**
     * Stores the position as the last known one and evaluates it against every safe zone of the
     * elderly profile. Crossings are published as {@code SafeZoneExitedEvent} /
     * {@code SafeZoneEnteredEvent}.
     *
     * @param command reported position
     * @return the updated last known location, or empty when the reading was older than the one
     *         already held and was discarded
     */
    Optional<LastKnownLocation> handle(EvaluateLocationCommand command);
}
