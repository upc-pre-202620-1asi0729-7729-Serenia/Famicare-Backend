package com.famicare.FamicareBackend.subscription.domain.model.commands;

import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.PlanType;

public record ChangePlanCommand(Long subscriptionId, PlanType planType) {
    public ChangePlanCommand {
        if (subscriptionId == null || subscriptionId <= 0)
            throw new IllegalArgumentException("subscription.error.id.invalid");
        if (planType == null) throw new IllegalArgumentException("subscription.error.plan.invalid");
    }
}
