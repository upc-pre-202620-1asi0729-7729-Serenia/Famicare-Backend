package com.famicare.FamicareBackend.subscription.domain.model.events;

import java.time.Instant;

public record SubscriptionCancellationScheduledEvent(Long subscriptionId, Instant effectiveAt) {}
