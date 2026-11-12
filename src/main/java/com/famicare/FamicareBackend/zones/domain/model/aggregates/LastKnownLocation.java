package com.famicare.FamicareBackend.zones.domain.model.aggregates;

import com.famicare.FamicareBackend.zones.domain.model.commands.EvaluateLocationCommand;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.zones.infrastructure.persistence.jpa.converters.ElderlyProfileIdAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Getter
@Entity
@Table(name = "last_known_locations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"elderly_profile_id"}, name = LastKnownLocation.ELDERLY_PROFILE_UNIQUE_CONSTRAINT)
        })
public class LastKnownLocation {
    public static final String ELDERLY_PROFILE_UNIQUE_CONSTRAINT = "uk_last_known_locations_elderly_profile_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = ElderlyProfileIdAttributeConverter.class)
    private ElderlyProfileId elderlyProfileId;

    @Embedded
    private Coordinates coordinates;

    @Column(nullable = false)
    private Instant recordedAt;

    protected LastKnownLocation() {}

    public LastKnownLocation(EvaluateLocationCommand command) {
        this.elderlyProfileId = command.elderlyProfileId();
        this.coordinates = command.coordinates();
        this.recordedAt = command.recordedAt();
    }

    /**
     * Replaces the held position with a newer one.
     *
     * @param newCoordinates reported position
     * @param newRecordedAt  instant at which the device produced the position
     */
    public boolean moveTo(Coordinates newCoordinates, Instant newRecordedAt) {
        if (newRecordedAt.isBefore(this.recordedAt)) {
            return false;
        }
        this.coordinates = newCoordinates;
        this.recordedAt = newRecordedAt;
        return true;
    }
}
