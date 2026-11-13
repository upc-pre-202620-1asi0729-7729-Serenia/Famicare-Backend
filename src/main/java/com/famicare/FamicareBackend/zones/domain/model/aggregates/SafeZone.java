package com.famicare.FamicareBackend.zones.domain.model.aggregates;

import com.famicare.FamicareBackend.zones.domain.model.commands.CreateSafeZoneCommand;
import com.famicare.FamicareBackend.zones.domain.model.commands.UpdateSafeZoneCommand;
import com.famicare.FamicareBackend.zones.domain.model.events.SafeZoneEnteredEvent;
import com.famicare.FamicareBackend.zones.domain.model.events.SafeZoneExitedEvent;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.*;
import com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa.converters.ElderlyProfileIdAttributeConverter;
import com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa.converters.ZoneNameAttributeConverter;
import com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa.converters.ZoneRadiusAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Instant;

/**
 * Aggregate root of the Zones bounded context. Maps the {@code safe_zones} table.
 * A safe zone is a circular geofence (center and radius) defined by a caregiver administrator
 * for an elderly profile. It remembers where the last evaluated coordinate stood
 * ({@link ZonePresence}) so it can tell when that coordinate crosses its boundary, raising a
 * {@link SafeZoneExitedEvent} or a {@link SafeZoneEnteredEvent}.
 */
@Getter
@Entity
@Table(name = "safe_zones",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"elderly_profile_id", "name"}, name = SafeZone.NAME_UNIQUE_CONSTRAINT)
        })
public class SafeZone extends AbstractAggregateRoot<SafeZone> {
    public static final String NAME_UNIQUE_CONSTRAINT = "uk_safe_zones_elderly_profile_name";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Reference to the Care context. */
    @Column(nullable = false)
    @Convert(converter = ElderlyProfileIdAttributeConverter.class)
    private ElderlyProfileId elderlyProfileId;

    @Column(nullable = false, length = 100)
    @Convert(converter = ZoneNameAttributeConverter.class)
    private ZoneName name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude",
                    column = @Column(name = "center_latitude", nullable = false, precision = 9, scale = 6)),
            @AttributeOverride(name = "longitude",
                    column = @Column(name = "center_longitude", nullable = false, precision = 9, scale = 6))
    })
    private Coordinates center;

    @Column(nullable = false)
    @Convert(converter = ZoneRadiusAttributeConverter.class)
    private ZoneRadius radius;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ZonePresence presence;

    /** Instant of the reading that produced the current {@link #presence}; {@code null} while unknown. */
    private Instant lastEvaluatedAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected SafeZone() {}

    /**
     * @param command zone definition
     * @param now     creation instant
     */
    public SafeZone(CreateSafeZoneCommand command, Instant now) {
        this.elderlyProfileId = command.elderlyProfileId();
        this.name = command.name();
        this.center = command.center();
        this.radius = command.radius();
        this.presence = ZonePresence.UNKNOWN;
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * @param position position to test
     * @return {@code true} when the position is within the radius of the center (boundary included)
     */
    public boolean contains(Coordinates position) {
        return center.distanceInMetersTo(position) <= radius.value();
    }

    /**
     * Replaces the name and the area of the zone. The presence is not touched here: the caller
     * decides whether to re-synchronize it with {@link #syncPresence}.
     *
     * @param command new name and area
     * @param now     update instant
     */
    public void update(UpdateSafeZoneCommand command, Instant now) {
        this.name = command.name();
        this.center = command.center();
        this.radius = command.radius();
        this.updatedAt = now;
    }

    /**
     * Sets the presence from a coordinate without raising any event. Used when the zone is created
     * or its area changes, so that a position the person already had is not reported as a crossing.
     *
     * @param position   last known position
     * @param recordedAt instant of that position
     */
    public void syncPresence(Coordinates position, Instant recordedAt) {
        this.presence = contains(position) ? ZonePresence.INSIDE : ZonePresence.OUTSIDE;
        this.lastEvaluatedAt = recordedAt;
    }

    /**
     * Evaluates a reported position against the zone boundary. A coordinate older than the last
     * evaluated one is ignored. Events are raised only on a real crossing: leaving from
     * {@link ZonePresence#INSIDE} or entering from {@link ZonePresence#OUTSIDE}; the first
     * evaluation ({@link ZonePresence#UNKNOWN}) only records the presence.
     *
     * @param position   reported position
     * @param recordedAt instant at which the device produced the position
     */
    public void evaluate(Coordinates position, Instant recordedAt) {
        if (lastEvaluatedAt != null && recordedAt.isBefore(lastEvaluatedAt)) {
            return;
        }
        var previous = this.presence;
        syncPresence(position, recordedAt);
        if (previous == ZonePresence.INSIDE && this.presence == ZonePresence.OUTSIDE) {
            registerEvent(new SafeZoneExitedEvent(id, elderlyProfileId, name.value(), position, recordedAt));
        } else if (previous == ZonePresence.OUTSIDE && this.presence == ZonePresence.INSIDE) {
            registerEvent(new SafeZoneEnteredEvent(id, elderlyProfileId, name.value(), position, recordedAt));
        }
    }
}
