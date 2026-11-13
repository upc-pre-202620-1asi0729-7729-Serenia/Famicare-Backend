package com.famicare.FamicareBackend.zones.application.commandservices;

/**
 * Failure types for safe zone command execution
 */
public sealed interface SafeZoneCommandFailure permits
        SafeZoneCommandFailure.SafeZoneNotFound,
        SafeZoneCommandFailure.DuplicateZoneName {
    /**
     * @return message key associated with the failure
     */
    String messageKey();

    /** No safe zone matches the given id. */
    record SafeZoneNotFound() implements SafeZoneCommandFailure {
        @Override
        public String messageKey() { return "zone.error.notFound"; }
    }

    /** The elderly profile already has a safe zone with that name. */
    record DuplicateZoneName() implements SafeZoneCommandFailure {
        @Override
        public String messageKey() { return "zone.error.name.duplicate"; }
    }
}
