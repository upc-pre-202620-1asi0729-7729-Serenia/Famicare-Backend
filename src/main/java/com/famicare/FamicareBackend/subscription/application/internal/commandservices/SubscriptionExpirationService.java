package com.famicare.FamicareBackend.subscription.application.internal.commandservices;

import com.famicare.FamicareBackend.subscription.infrastructure.persistence.jpa.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
public class SubscriptionExpirationService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionExpirationService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Scheduled(fixedDelayString = "${subscriptions.expiration-check-ms:60000}")
    @Transactional
    public void expireDueSubscriptions() {
        var now = Instant.now();
        var dueSubscriptions = subscriptionRepository
                .findAllByCancelAtPeriodEndTrueAndCancelEffectiveDateLessThanEqual(now);
        dueSubscriptions.forEach(subscription -> {
            if (subscription.expireIfDue(now)) {
                subscriptionRepository.save(subscription);
                log.info("Subscription expired: id={}, userId={}",
                        subscription.getId(), subscription.getUserId().value());
            }
        });
    }
}
