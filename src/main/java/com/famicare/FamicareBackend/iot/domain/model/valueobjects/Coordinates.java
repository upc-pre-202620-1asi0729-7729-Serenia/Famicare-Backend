package com.famicare.FamicareBackend.iot.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Embeddable value object representing a GPS position (decimal(9,6) columns).
 */
@Getter
@Embeddable
public class Coordinates {
    private static final BigDecimal MAX_LATITUDE = BigDecimal.valueOf(90);
    private static final BigDecimal MAX_LONGITUDE = BigDecimal.valueOf(180);
    private static final String LATITUDE_MESSAGE_KEY = "coordinates.error.latitude.invalid";
    private static final String LONGITUDE_MESSAGE_KEY = "coordinates.error.longitude.invalid";

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    protected Coordinates() {}

    public Coordinates(BigDecimal latitude, BigDecimal longitude) {
        if (latitude == null || latitude.abs().compareTo(MAX_LATITUDE) > 0) {
            throw new IllegalArgumentException(LATITUDE_MESSAGE_KEY);
        }
        if (longitude == null || longitude.abs().compareTo(MAX_LONGITUDE) > 0) {
            throw new IllegalArgumentException(LONGITUDE_MESSAGE_KEY);
        }
        this.latitude = latitude.setScale(6, RoundingMode.HALF_UP);
        this.longitude = longitude.setScale(6, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Coordinates that)) return false;
        return Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
