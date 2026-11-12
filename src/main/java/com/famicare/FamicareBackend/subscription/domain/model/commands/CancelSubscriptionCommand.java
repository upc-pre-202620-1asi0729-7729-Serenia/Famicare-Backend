package com.famicare.FamicareBackend.subscription.domain.model.commands;

public record CancelSubscriptionCommand(Long subscriptionId) {
    public CancelSubscriptionCommand {
        if (subscriptionId == null || subscriptionId <= 0)
            throw new IllegalArgumentException("subscription.error.id.invalid");
    }
}
