package com.famicare.FamicareBackend.subscription.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;

public record SubscriptionResource(
        Long id,
        Long userId,
        String planType,
        String planDisplayName,
        BigDecimal priceMonthly,
        String currency,
        String status,
        Instant renewalDate,
        boolean cancelAtPeriodEnd,
        Instant cancelEffectiveDate,
        PaymentMethodSummaryResource paymentMethod,
        Instant createdAt,
        Instant updatedAt
) {}
