package com.famicare.FamicareBackend.subscription.domain.model.queries;

public record GetSubscriptionByIdQuery(Long subscriptionId) {
    public GetSubscriptionByIdQuery {
        if (subscriptionId == null || subscriptionId <= 0)
            throw new IllegalArgumentException("subscription.error.id.invalid");
    }
}
