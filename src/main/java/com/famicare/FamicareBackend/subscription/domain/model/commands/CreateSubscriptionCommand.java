package com.famicare.FamicareBackend.subscription.domain.model.commands;

import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.PaymentMethod;
import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.PlanType;
import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.UserId;

public record CreateSubscriptionCommand(UserId userId, PlanType planType, PaymentMethod paymentMethod) {
    public CreateSubscriptionCommand {
        if (userId == null) throw new IllegalArgumentException("subscription.error.userId.invalid");
        if (planType == null) throw new IllegalArgumentException("subscription.error.plan.invalid");
    }
}
