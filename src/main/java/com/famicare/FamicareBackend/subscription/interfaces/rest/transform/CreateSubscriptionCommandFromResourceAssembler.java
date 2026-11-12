package com.famicare.FamicareBackend.subscription.interfaces.rest.transform;

import com.famicare.FamicareBackend.subscription.domain.model.commands.CreateSubscriptionCommand;
import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.UserId;
import com.famicare.FamicareBackend.subscription.interfaces.rest.resources.CreateSubscriptionResource;

public final class CreateSubscriptionCommandFromResourceAssembler {
    private CreateSubscriptionCommandFromResourceAssembler() {}

    public static CreateSubscriptionCommand toCommand(CreateSubscriptionResource resource) {
        return new CreateSubscriptionCommand(
                new UserId(resource.userId()),
                SubscriptionValueObjectAssembler.toPlanType(resource.planType()),
                PaymentMethodFromResourceAssembler.toValueObject(resource.paymentMethod()));
    }
}
