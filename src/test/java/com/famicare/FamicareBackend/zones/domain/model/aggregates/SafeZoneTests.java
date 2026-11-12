package com.famicare.FamicareBackend.zones.domain.model.aggregates;

import com.famicare.FamicareBackend.zones.domain.model.commands.CreateSafeZoneCommand;
import com.famicare.FamicareBackend.zones.domain.model.events.SafeZoneEnteredEvent;
import com.famicare.FamicareBackend.zones.domain.model.events.SafeZoneExitedEvent;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class SafeZoneTests {
    private final Instant now = Instant.parse("2026-11-11T12:00:00Z");
    private final Coordinates home = coordinates("-12.046374", "-77.042793");
    // ~55 m north of home
    private final Coordinates nearHome = coordinates("-12.045877", "-77.042793");
    // ~1.1 km north of home
    private final Coordinates farAway = coordinates("-12.036374", "-77.042793");

    @Test
    void startsWithUnknownPresence() {
        var zone = homeZone();

        assertEquals(ZonePresence.UNKNOWN, zone.getPresence());
        assertNull(zone.getLastEvaluatedAt());
    }

    @Test
    void containsPositionsWithinRadiusOnly() {
        var zone = homeZone();

        assertTrue(zone.contains(home));
        assertTrue(zone.contains(nearHome));
        assertFalse(zone.contains(farAway));
    }

    @Test
    void firstEvaluationRecordsPresenceWithoutRaisingEvents() {
        var zone = homeZone();

        zone.evaluate(farAway, now);

        assertEquals(ZonePresence.OUTSIDE, zone.getPresence());
        assertTrue(events(zone).isEmpty());
    }

    @Test
    void raisesExitEventWhenLeavingTheZone() {
        var zone = homeZone();
        zone.evaluate(home, now);

        zone.evaluate(farAway, now.plusSeconds(60));

        assertEquals(ZonePresence.OUTSIDE, zone.getPresence());
        var raised = events(zone);
        assertEquals(1, raised.size());
        var event = assertInstanceOf(SafeZoneExitedEvent.class, raised.iterator().next());
        assertEquals("Casa", event.zoneName());
        assertEquals(new ElderlyProfileId(1L), event.elderlyProfileId());
        assertEquals(now.plusSeconds(60), event.recordedAt());
    }

    @Test
    void raisesEnterEventWhenReturningToTheZone() {
        var zone = homeZone();
        zone.evaluate(farAway, now);

        zone.evaluate(nearHome, now.plusSeconds(60));

        assertEquals(ZonePresence.INSIDE, zone.getPresence());
        assertInstanceOf(SafeZoneEnteredEvent.class, events(zone).iterator().next());
    }

    @Test
    void doesNotRaiseEventsWhilePresenceDoesNotChange() {
        var zone = homeZone();
        zone.evaluate(home, now);

        zone.evaluate(nearHome, now.plusSeconds(60));

        assertEquals(ZonePresence.INSIDE, zone.getPresence());
        assertTrue(events(zone).isEmpty());
    }

    @Test
    void ignoresReadingsOlderThanTheLastEvaluatedOne() {
        var zone = homeZone();
        zone.evaluate(home, now);

        zone.evaluate(farAway, now.minusSeconds(60));

        assertEquals(ZonePresence.INSIDE, zone.getPresence());
        assertEquals(now, zone.getLastEvaluatedAt());
        assertTrue(events(zone).isEmpty());
    }

    @Test
    void syncPresenceDoesNotRaiseEvents() {
        var zone = homeZone();
        zone.evaluate(home, now);

        zone.syncPresence(farAway, now.plusSeconds(60));

        assertEquals(ZonePresence.OUTSIDE, zone.getPresence());
        assertTrue(events(zone).isEmpty());
    }

    @Test
    void calculatesHaversineDistance() {
        // one degree of latitude is about 111.2 km
        var distance = coordinates("0", "0").distanceInMetersTo(coordinates("1", "0"));

        assertEquals(111_195.0, distance, 100.0);
    }

    @Test
    void rejectsRadiusOutOfRange() {
        var tooSmall = assertThrows(IllegalArgumentException.class, () -> new ZoneRadius(10));
        var tooBig = assertThrows(IllegalArgumentException.class, () -> new ZoneRadius(10_001));

        assertEquals("zone.error.radius.range", tooSmall.getMessage());
        assertEquals("zone.error.radius.range", tooBig.getMessage());
    }

    @Test
    void rejectsBlankName() {
        var exception = assertThrows(IllegalArgumentException.class, () -> new ZoneName("  "));

        assertEquals("zone.error.name.notBlank", exception.getMessage());
    }

    @Test
    void lastKnownLocationDiscardsOlderReadings() {
        var location = new LastKnownLocation(new com.famicare.FamicareBackend.zones.domain.model.commands
                .EvaluateLocationCommand(new ElderlyProfileId(1L), home, now));

        assertFalse(location.moveTo(farAway, now.minusSeconds(1)));
        assertEquals(home, location.getCoordinates());
        assertTrue(location.moveTo(farAway, now.plusSeconds(1)));
        assertEquals(farAway, location.getCoordinates());
    }

    private SafeZone homeZone() {
        return new SafeZone(new CreateSafeZoneCommand(
                new ElderlyProfileId(1L), new ZoneName("Casa"), home, new ZoneRadius(100)), now);
    }

    private static Coordinates coordinates(String latitude, String longitude) {
        return new Coordinates(new BigDecimal(latitude), new BigDecimal(longitude));
    }

    @SuppressWarnings("unchecked")
    private static Collection<Object> events(SafeZone zone) {
        return (Collection<Object>) ReflectionTestUtils.invokeMethod(zone, "domainEvents");
    }
}
