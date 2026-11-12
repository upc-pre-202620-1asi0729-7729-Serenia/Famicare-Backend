package com.famicare.FamicareBackend.subscription.interfaces.rest.transform;

import com.famicare.FamicareBackend.subscription.domain.model.aggregates.Subscription;
import com.famicare.FamicareBackend.subscription.interfaces.rest.resources.PaymentMethodSummaryResource;
import com.famicare.FamicareBackend.subscription.interfaces.rest.resources.SubscriptionResource;

public final class SubscriptionResourceFromEntityAssembler {
    private SubscriptionResourceFromEntityAssembler() {}

    public static SubscriptionResource toResource(Subscription entity) {
        PaymentMethodSummaryResource payment = null;
        if (entity.getPaymentMethod() != null) {
            var method = entity.getPaymentMethod();
            payment = new PaymentMethodSummaryResource(method.getBrand(), method.getLastFour(),
                    method.getExpirationMonth(), method.getExpirationYear());
        }
        return new SubscriptionResource(
                entity.getId(),
                entity.getUserId().value(),
                entity.getPlanType().name(),
                entity.getPlanType().displayName(),
                entity.getPriceMonthly(),
                entity.getCurrency(),
                entity.getStatus().name(),
                entity.getCurrentPeriodEnd(),
                entity.isCancelAtPeriodEnd(),
                entity.getCancelEffectiveDate(),
                payment,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
