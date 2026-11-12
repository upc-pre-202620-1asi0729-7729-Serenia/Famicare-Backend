package com.famicare.FamicareBackend.subscription.interfaces.rest.transform;

import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.PlanType;

import java.util.Locale;

public final class SubscriptionValueObjectAssembler {
    private SubscriptionValueObjectAssembler() {}

    public static PlanType toPlanType(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("subscription.error.plan.invalid");
        try {
            var normalized = value.trim().toUpperCase(Locale.ROOT)
                    .replace(' ', '_').replace('-', '_');
            return PlanType.valueOf(normalized);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("subscription.error.plan.invalid");
        }
    }
}
