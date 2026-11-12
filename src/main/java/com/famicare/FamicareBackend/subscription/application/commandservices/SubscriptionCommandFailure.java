package com.famicare.FamicareBackend.subscription.application.commandservices;

public sealed interface SubscriptionCommandFailure permits
        SubscriptionCommandFailure.SubscriptionNotFound,
        SubscriptionCommandFailure.SubscriptionAlreadyExists,
        SubscriptionCommandFailure.PaymentMethodRequired,
        SubscriptionCommandFailure.CancellationAlreadyScheduled,
        SubscriptionCommandFailure.FreePlanCannotBeCancelled {

    String messageKey();

    record SubscriptionNotFound() implements SubscriptionCommandFailure {
        public String messageKey() { return "subscription.error.notFound"; }
    }
    record SubscriptionAlreadyExists() implements SubscriptionCommandFailure {
        public String messageKey() { return "subscription.error.alreadyExists"; }
    }
    record PaymentMethodRequired() implements SubscriptionCommandFailure {
        public String messageKey() { return "subscription.error.paymentMethod.required"; }
    }
    record CancellationAlreadyScheduled() implements SubscriptionCommandFailure {
        public String messageKey() { return "subscription.error.cancellation.alreadyScheduled"; }
    }
    record FreePlanCannotBeCancelled() implements SubscriptionCommandFailure {
        public String messageKey() { return "subscription.error.freePlan.cannotCancel"; }
    }
}
