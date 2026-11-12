package com.famicare.FamicareBackend.subscription.domain.model.commands;

import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.PaymentMethod;

public record UpdatePaymentMethodCommand(Long subscriptionId, PaymentMethod paymentMethod) {
    public UpdatePaymentMethodCommand {
        if (subscriptionId == null || subscriptionId <= 0)
            throw new IllegalArgumentException("subscription.error.id.invalid");
        if (paymentMethod == null)
            throw new IllegalArgumentException("subscription.error.paymentMethod.required");
    }
}
