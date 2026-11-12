package com.famicare.FamicareBackend.monitor.domain.model.aggregates;

import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.ElderlyProfileId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "location_records")
@Getter
@NoArgsConstructor
public class LocationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ElderlyProfileId elderlyProfileId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private Double accuracy;
    private String placeName;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    public LocationRecord(ElderlyProfileId elderlyProfileId, Double latitude, Double longitude, Double accuracy, String placeName) {
        this.elderlyProfileId = elderlyProfileId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy != null ? accuracy : 5.0;
        this.placeName = placeName != null ? placeName : "Ubicacion detectada";
        this.recordedAt = LocalDateTime.now();
    }
}