package com.famicare.FamicareBackend.care.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record EmailAddress(
        @Column(name = "email", nullable = false, length = 150)
        String value
) {
    public EmailAddress {
        if (value == null || value.isBlank() || !value.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        value = value.trim().toLowerCase();
    }
}