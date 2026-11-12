package com.famicare.FamicareBackend.subscription.domain.model.aggregates;

import com.famicare.FamicareBackend.subscription.domain.model.commands.CreateSubscriptionCommand;
import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionTests {
    private final Instant now = Instant.parse("2026-11-11T12:00:00Z");

    @Test
    void createsPaidSubscriptionWithTokenizedPaymentMethod() {
        var subscription = premiumSubscription();

        assertEquals(PlanType.FAMILIAR_PREMIUM, subscription.getPlanType());
        assertEquals("29.99", subscription.getPriceMonthly().toPlainString());
        assertEquals("PEN", subscription.getCurrency());
        assertEquals("4321", subscription.getPaymentMethod().getLastFour());
        assertFalse(subscription.isCancelAtPeriodEnd());
    }

    @Test
    void rejectsPaidSubscriptionWithoutPaymentMethod() {
        var command = new CreateSubscriptionCommand(
                new UserId(1L), PlanType.FAMILIAR_PLUS, null);

        var exception = assertThrows(IllegalArgumentException.class,
                () -> new Subscription(command, now));

        assertEquals("subscription.error.paymentMethod.required", exception.getMessage());
    }

    @Test
    void schedulesCancellationAtCurrentPeriodEnd() {
        var subscription = premiumSubscription();

        subscription.scheduleCancellation(now.plusSeconds(60));

        assertTrue(subscription.isCancelAtPeriodEnd());
        assertEquals(subscription.getCurrentPeriodEnd(), subscription.getCancelEffectiveDate());
        assertEquals(PlanType.FAMILIAR_PREMIUM, subscription.getPlanType());
    }

    @Test
    void expiresCancelledSubscriptionAndRemovesPaymentMethod() {
        var subscription = premiumSubscription();
        subscription.scheduleCancellation(now);

        var expired = subscription.expireIfDue(now.plusSeconds(40L * 24 * 60 * 60));

        assertTrue(expired);
        assertEquals(PlanType.FREE, subscription.getPlanType());
        assertEquals(SubscriptionStatus.EXPIRED, subscription.getStatus());
        assertNull(subscription.getPaymentMethod());
        assertFalse(subscription.isCancelAtPeriodEnd());
    }

    @Test
    void changingPlanClearsScheduledCancellation() {
        var subscription = premiumSubscription();
        subscription.scheduleCancellation(now);

        subscription.changePlan(PlanType.FAMILIAR_PLUS, now.plusSeconds(120));

        assertEquals(PlanType.FAMILIAR_PLUS, subscription.getPlanType());
        assertEquals("18.99", subscription.getPriceMonthly().toPlainString());
        assertFalse(subscription.isCancelAtPeriodEnd());
        assertNull(subscription.getCancelEffectiveDate());
    }

    private Subscription premiumSubscription() {
        var expiry = YearMonth.now().plusYears(2);
        var paymentMethod = new PaymentMethod(
                "pm_test_123", "Visa", "4321", expiry.getMonthValue(), expiry.getYear());
        var command = new CreateSubscriptionCommand(
                new UserId(1L), PlanType.FAMILIAR_PREMIUM, paymentMethod);
        return new Subscription(command, now);
    }
}
