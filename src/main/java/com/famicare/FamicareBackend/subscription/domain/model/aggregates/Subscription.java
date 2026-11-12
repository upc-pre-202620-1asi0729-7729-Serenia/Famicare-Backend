package com.famicare.FamicareBackend.subscription.domain.model.aggregates;

import com.famicare.FamicareBackend.subscription.domain.model.commands.CreateSubscriptionCommand;
import com.famicare.FamicareBackend.subscription.domain.model.events.SubscriptionCancellationScheduledEvent;
import com.famicare.FamicareBackend.subscription.domain.model.events.SubscriptionExpiredEvent;
import com.famicare.FamicareBackend.subscription.domain.model.events.SubscriptionPlanChangedEvent;
import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.PaymentMethod;
import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.PlanType;
import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.SubscriptionStatus;
import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.UserId;
import com.famicare.FamicareBackend.subscription.infrastructure.persistence.jpa.converters.UserIdAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "subscriptions",
        uniqueConstraints = @UniqueConstraint(
                name = Subscription.USER_ID_UNIQUE_CONSTRAINT,
                columnNames = "user_id"),
        indexes = @Index(name = "idx_subscriptions_status", columnList = "status"))
public class Subscription extends AbstractAggregateRoot<Subscription> {
    public static final String USER_ID_UNIQUE_CONSTRAINT = "uk_subscriptions_user_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = UserIdAttributeConverter.class)
    @Column(nullable = false)
    private UserId userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PlanType planType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceMonthly;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private Instant currentPeriodEnd;

    @Column(nullable = false)
    private boolean cancelAtPeriodEnd;

    private Instant cancelEffectiveDate;

    @Embedded
    private PaymentMethod paymentMethod;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Subscription() {}

    public Subscription(CreateSubscriptionCommand command, Instant now) {
        if (command.planType().isPaid() && command.paymentMethod() == null) {
            throw new IllegalArgumentException("subscription.error.paymentMethod.required");
        }
        this.userId = command.userId();
        this.planType = command.planType();
        this.priceMonthly = command.planType().monthlyPrice();
        this.currency = "PEN";
        this.status = SubscriptionStatus.ACTIVE;
        this.currentPeriodEnd = nextPeriodEnd(now);
        this.cancelAtPeriodEnd = false;
        this.paymentMethod = command.paymentMethod();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void changePlan(PlanType newPlan, Instant now) {
        if (newPlan.isPaid() && paymentMethod == null) {
            throw new IllegalStateException("subscription.error.paymentMethod.required");
        }
        var previousPlan = this.planType;
        this.planType = newPlan;
        this.priceMonthly = newPlan.monthlyPrice();
        this.status = SubscriptionStatus.ACTIVE;
        this.currentPeriodEnd = nextPeriodEnd(now);
        this.cancelAtPeriodEnd = false;
        this.cancelEffectiveDate = null;
        this.updatedAt = now;
        if (newPlan == PlanType.FREE) this.paymentMethod = null;
        registerEvent(new SubscriptionPlanChangedEvent(id, previousPlan, newPlan, now));
    }

    public void updatePaymentMethod(PaymentMethod paymentMethod, Instant now) {
        if (paymentMethod == null)
            throw new IllegalArgumentException("subscription.error.paymentMethod.required");
        this.paymentMethod = paymentMethod;
        this.updatedAt = now;
    }

    public void scheduleCancellation(Instant now) {
        if (!planType.isPaid())
            throw new IllegalStateException("subscription.error.freePlan.cannotCancel");
        if (cancelAtPeriodEnd)
            throw new IllegalStateException("subscription.error.cancellation.alreadyScheduled");
        this.cancelAtPeriodEnd = true;
        this.cancelEffectiveDate = currentPeriodEnd;
        this.updatedAt = now;
        registerEvent(new SubscriptionCancellationScheduledEvent(id, cancelEffectiveDate));
    }

    public boolean expireIfDue(Instant now) {
        if (!cancelAtPeriodEnd || cancelEffectiveDate == null || now.isBefore(cancelEffectiveDate)) {
            return false;
        }
        this.planType = PlanType.FREE;
        this.priceMonthly = PlanType.FREE.monthlyPrice();
        this.status = SubscriptionStatus.EXPIRED;
        this.cancelAtPeriodEnd = false;
        this.cancelEffectiveDate = null;
        this.paymentMethod = null;
        this.updatedAt = now;
        registerEvent(new SubscriptionExpiredEvent(id, now));
        return true;
    }

    private static Instant nextPeriodEnd(Instant from) {
        return ZonedDateTime.ofInstant(from, ZoneOffset.UTC).plusMonths(1).toInstant();
    }
}
