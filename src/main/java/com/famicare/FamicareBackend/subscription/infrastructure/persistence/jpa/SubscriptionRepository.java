package com.famicare.FamicareBackend.subscription.infrastructure.persistence.jpa;

import com.famicare.FamicareBackend.subscription.domain.model.aggregates.Subscription;
import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserId(UserId userId);
    boolean existsByUserId(UserId userId);
    List<Subscription> findAllByCancelAtPeriodEndTrueAndCancelEffectiveDateLessThanEqual(Instant now);
}
