package com.famicare.FamicareBackend.iot.domain.model.aggregates;

import com.famicare.FamicareBackend.iot.domain.model.commands.RecordTelemetryCommand;
import com.famicare.FamicareBackend.iot.domain.model.events.TelemetryReceivedEvent;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.BatteryLevel;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.Coordinates;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.TelemetryEventType;
import com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa.converters.BatteryLevelAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Instant;

/**
 * Append-only record of a message sent by an IoT device. Maps the {@code telemetry_events} table.
 * It references its device by id (separate aggregate) and raises a
 * {@link TelemetryReceivedEvent} so other contexts can react to it.
 */
@Getter
@Entity
@Table(name = "telemetry_events",
        indexes = {
                @Index(name = "idx_telemetry_events_device_recorded_at", columnList = "device_id, recorded_at")
        })
public class TelemetryEvent extends AbstractAggregateRoot<TelemetryEvent> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long deviceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TelemetryEventType eventType;

    @Embedded
    private Coordinates coordinates;

    @Column(columnDefinition = "tinyint")
    @Convert(converter = BatteryLevelAttributeConverter.class)
    private BatteryLevel batteryLevel;

    @Column(nullable = false)
    private Instant recordedAt;

    @Column(nullable = false, updatable = false)
    private Instant receivedAt;

    protected TelemetryEvent() {}

    /**
     * @param device  paired device that sent the message
     * @param command telemetry data
     */
    public TelemetryEvent(Device device, RecordTelemetryCommand command) {
        this.deviceId = device.getId();
        this.eventType = command.eventType();
        this.coordinates = command.coordinates();
        this.batteryLevel = command.batteryLevel();
        this.recordedAt = command.recordedAt();
        this.receivedAt = Instant.now();
        registerEvent(new TelemetryReceivedEvent(
                this.deviceId,
                device.getElderlyProfileId(),
                this.eventType,
                this.coordinates,
                this.batteryLevel,
                this.recordedAt,
                this.receivedAt));
    }
}
