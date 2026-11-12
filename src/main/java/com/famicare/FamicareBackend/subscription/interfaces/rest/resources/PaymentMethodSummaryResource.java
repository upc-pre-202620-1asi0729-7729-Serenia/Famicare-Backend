package com.famicare.FamicareBackend.subscription.interfaces.rest.resources;

public record PaymentMethodSummaryResource(
        String brand,
        String lastFour,
        Integer expirationMonth,
        Integer expirationYear
) {}
