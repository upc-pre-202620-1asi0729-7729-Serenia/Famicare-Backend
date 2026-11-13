package com.famicare.FamicareBackend.iot.domain.model.aggregates;

import com.famicare.FamicareBackend.iot.domain.model.commands.RegisterDeviceCommand;
import com.famicare.FamicareBackend.iot.domain.model.events.DevicePairedEvent;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.BatteryLevel;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.DeviceStatus;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.PairingCode;
import com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa.converters.BatteryLevelAttributeConverter;
import com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa.converters.ElderlyProfileIdAttributeConverter;
import com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa.converters.PairingCodeAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Duration;
import java.time.Instant;

/**
 * Aggregate root of the IoT bounded context. Maps the {@code iot_devices} table.
 * A device is registered with a pairing code ({@link DeviceStatus#PENDING}) and later
 * linked to an elderly profile ({@link DeviceStatus#PAIRED}). While paired it reports
 * its battery level and heartbeat through telemetry.
 */
@Getter
@Entity
@Table(name = "iot_devices",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"pairing_code"}, name = Device.PAIRING_CODE_UNIQUE_CONSTRAINT)
        },
        indexes = {
                @Index(name = "idx_iot_devices_elderly_profile_id", columnList = "elderly_profile_id")
        })
public class Device extends AbstractAggregateRoot<Device> {
    public static final String PAIRING_CODE_UNIQUE_CONSTRAINT = "uk_iot_devices_pairing_code";

    /** A device is considered online when its last heartbeat is not older than this. */
    public static final Duration ONLINE_THRESHOLD = Duration.ofMinutes(5);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Reference to the Care context; {@code null} until the device is paired. */
    @Convert(converter = ElderlyProfileIdAttributeConverter.class)
    private ElderlyProfileId elderlyProfileId;

    @Column(nullable = false, length = 32)
    @Convert(converter = PairingCodeAttributeConverter.class)
    private PairingCode pairingCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeviceStatus status;

    @Convert(converter = BatteryLevelAttributeConverter.class)
    private BatteryLevel batteryLevel;

    private Instant lastHeartbeat;

    private Instant pairedAt;

    protected Device() {}

    public Device(RegisterDeviceCommand command) {
        this.pairingCode = command.pairingCode();
        this.status = DeviceStatus.PENDING;
    }

    public boolean isPaired() {
        return status == DeviceStatus.PAIRED;
    }

    /**
     * Links the device to an elderly profile.
     *
     * @param elderlyProfileId elderly profile that will own the device
     * @throws IllegalStateException if the device is already paired
     */
    public void pairTo(ElderlyProfileId elderlyProfileId) {
        if (isPaired()) {
            throw new IllegalStateException("device.error.alreadyPaired");
        }
        this.elderlyProfileId = elderlyProfileId;
        this.status = DeviceStatus.PAIRED;
        this.pairedAt = Instant.now();
        registerEvent(new DevicePairedEvent(this.id, elderlyProfileId, this.pairedAt));
    }

    /**
     * Registers that the device was heard from. Any telemetry counts as a heartbeat.
     *
     * @param batteryLevel battery level reported, or {@code null} when the message did not carry one
     * @param receivedAt   server instant at which the message arrived
     */
    public void registerHeartbeat(BatteryLevel batteryLevel, Instant receivedAt) {
        this.lastHeartbeat = receivedAt;
        if (batteryLevel != null) {
            this.batteryLevel = batteryLevel;
        }
    }

    /**
     * @param now reference instant
     * @return {@code true} when the device is paired and its last heartbeat is recent enough
     */
    public boolean isOnline(Instant now) {
        return isPaired()
                && lastHeartbeat != null
                && !lastHeartbeat.plus(ONLINE_THRESHOLD).isBefore(now);
    }
}
