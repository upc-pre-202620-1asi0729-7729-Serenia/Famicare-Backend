package com.famicare.FamicareBackend.subscription.application.commandservices;

import com.famicare.FamicareBackend.shared.application.result.Result;
import com.famicare.FamicareBackend.subscription.domain.model.aggregates.Subscription;
import com.famicare.FamicareBackend.subscription.domain.model.commands.*;

public interface SubscriptionCommandService {
    Result<Subscription, SubscriptionCommandFailure> handle(CreateSubscriptionCommand command);
    Result<Subscription, SubscriptionCommandFailure> handle(ChangePlanCommand command);
    Result<Subscription, SubscriptionCommandFailure> handle(UpdatePaymentMethodCommand command);
    Result<Subscription, SubscriptionCommandFailure> handle(CancelSubscriptionCommand command);
}
