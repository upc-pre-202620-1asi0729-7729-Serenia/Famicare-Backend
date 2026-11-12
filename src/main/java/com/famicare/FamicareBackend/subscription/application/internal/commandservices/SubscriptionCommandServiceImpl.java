package com.famicare.FamicareBackend.subscription.application.internal.commandservices;

import com.famicare.FamicareBackend.shared.application.result.Result;
import com.famicare.FamicareBackend.subscription.application.commandservices.SubscriptionCommandFailure;
import com.famicare.FamicareBackend.subscription.application.commandservices.SubscriptionCommandService;
import com.famicare.FamicareBackend.subscription.domain.model.aggregates.Subscription;
import com.famicare.FamicareBackend.subscription.domain.model.commands.*;
import com.famicare.FamicareBackend.subscription.infrastructure.persistence.jpa.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Result<Subscription, SubscriptionCommandFailure> handle(CreateSubscriptionCommand command) {
        if (subscriptionRepository.existsByUserId(command.userId())) {
            return Result.fallido(new SubscriptionCommandFailure.SubscriptionAlreadyExists());
        }
        if (command.planType().isPaid() && command.paymentMethod() == null) {
            return Result.fallido(new SubscriptionCommandFailure.PaymentMethodRequired());
        }
        try {
            var subscription = new Subscription(command, Instant.now());
            var created = subscriptionRepository.saveAndFlush(subscription);
            log.info("Subscription created: id={}, userId={}, plan={}",
                    created.getId(), command.userId().value(), created.getPlanType());
            return Result.logrado(created);
        } catch (DataIntegrityViolationException exception) {
            if (isDuplicateUserViolation(exception)) {
                return Result.fallido(new SubscriptionCommandFailure.SubscriptionAlreadyExists());
            }
            throw exception;
        }
    }

    @Override
    @Transactional
    public Result<Subscription, SubscriptionCommandFailure> handle(ChangePlanCommand command) {
        var subscription = subscriptionRepository.findById(command.subscriptionId());
        if (subscription.isEmpty())
            return Result.fallido(new SubscriptionCommandFailure.SubscriptionNotFound());
        if (command.planType().isPaid() && subscription.get().getPaymentMethod() == null)
            return Result.fallido(new SubscriptionCommandFailure.PaymentMethodRequired());

        subscription.get().changePlan(command.planType(), Instant.now());
        return Result.logrado(subscriptionRepository.save(subscription.get()));
    }

    @Override
    @Transactional
    public Result<Subscription, SubscriptionCommandFailure> handle(UpdatePaymentMethodCommand command) {
        var subscription = subscriptionRepository.findById(command.subscriptionId());
        if (subscription.isEmpty())
            return Result.fallido(new SubscriptionCommandFailure.SubscriptionNotFound());

        subscription.get().updatePaymentMethod(command.paymentMethod(), Instant.now());
        return Result.logrado(subscriptionRepository.save(subscription.get()));
    }

    @Override
    @Transactional
    public Result<Subscription, SubscriptionCommandFailure> handle(CancelSubscriptionCommand command) {
        var subscription = subscriptionRepository.findById(command.subscriptionId());
        if (subscription.isEmpty())
            return Result.fallido(new SubscriptionCommandFailure.SubscriptionNotFound());
        if (!subscription.get().getPlanType().isPaid())
            return Result.fallido(new SubscriptionCommandFailure.FreePlanCannotBeCancelled());
        if (subscription.get().isCancelAtPeriodEnd())
            return Result.fallido(new SubscriptionCommandFailure.CancellationAlreadyScheduled());

        subscription.get().scheduleCancellation(Instant.now());
        return Result.logrado(subscriptionRepository.save(subscription.get()));
    }

    private boolean isDuplicateUserViolation(DataIntegrityViolationException exception) {
        Throwable cause = exception;
        while (cause != null) {
            if (cause.getMessage() != null
                    && cause.getMessage().contains(Subscription.USER_ID_UNIQUE_CONSTRAINT)) return true;
            cause = cause.getCause();
        }
        return false;
    }
}
