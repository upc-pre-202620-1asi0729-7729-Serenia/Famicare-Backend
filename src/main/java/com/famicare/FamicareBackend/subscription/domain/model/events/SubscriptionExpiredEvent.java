package com.famicare.FamicareBackend.subscription.domain.model.events;

import java.time.Instant;

public record SubscriptionExpiredEvent(Long subscriptionId, Instant expiredAt) {}
