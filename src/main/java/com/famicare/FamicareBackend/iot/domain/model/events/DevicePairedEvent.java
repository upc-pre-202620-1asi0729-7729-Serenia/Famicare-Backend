package com.famicare.FamicareBackend.iot.domain.model.events;

import com.famicare.FamicareBackend.iot.domain.model.valueobjects.ElderlyProfileId;

import java.time.Instant;

/**
 * Domain event raised when a device is linked to an elderly profile.
 */
public record DevicePairedEvent(Long deviceId, ElderlyProfileId elderlyProfileId, Instant pairedAt) {
}
