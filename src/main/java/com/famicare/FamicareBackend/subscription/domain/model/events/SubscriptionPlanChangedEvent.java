package com.famicare.FamicareBackend.subscription.domain.model.events;

import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.PlanType;
import java.time.Instant;

public record SubscriptionPlanChangedEvent(Long subscriptionId, PlanType previousPlan,
                                           PlanType newPlan, Instant changedAt) {}
