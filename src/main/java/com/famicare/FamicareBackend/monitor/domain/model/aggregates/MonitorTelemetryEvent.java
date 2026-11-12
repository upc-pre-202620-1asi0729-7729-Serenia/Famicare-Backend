package com.famicare.FamicareBackend.monitor.domain.model.aggregates;

import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.TelemetryEventType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitor_telemetry_events")
@Getter
@NoArgsConstructor
public class MonitorTelemetryEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ElderlyProfileId elderlyProfileId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TelemetryEventType eventType;

    private String details;

    @Column(nullable = false)
    private LocalDateTime reportedAt;

    public MonitorTelemetryEvent(ElderlyProfileId elderlyProfileId, TelemetryEventType eventType, String details) {
        this.elderlyProfileId = elderlyProfileId;
        this.eventType = eventType;
        this.details = details;
        this.reportedAt = LocalDateTime.now();
    }
}