package com.famicare.FamicareBackend.zones.domain.model.valuesobjects;

/**
 * Where the last known coordinate of an elderly person stands relative to a safe zone
 * (column {@code presence} of {@code safe_zones}).
 */
public enum ZonePresence {
    /** No coordinate has been evaluated against the zone yet. */
    UNKNOWN,
    /** The last known coordinate lies inside the zone boundary. */
    INSIDE,
    /** The last known coordinate lies outside the zone boundary. */
    OUTSIDE
}
