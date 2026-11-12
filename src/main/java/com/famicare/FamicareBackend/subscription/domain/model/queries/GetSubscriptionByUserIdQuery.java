package com.famicare.FamicareBackend.subscription.domain.model.queries;

import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.UserId;

public record GetSubscriptionByUserIdQuery(UserId userId) {
    public GetSubscriptionByUserIdQuery {
        if (userId == null) throw new IllegalArgumentException("subscription.error.userId.invalid");
    }
}
