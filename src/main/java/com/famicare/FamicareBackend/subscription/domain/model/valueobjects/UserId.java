package com.famicare.FamicareBackend.subscription.domain.model.valueobjects;

public record UserId(Long value) {
    public UserId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("subscription.error.userId.invalid");
        }
    }
}
