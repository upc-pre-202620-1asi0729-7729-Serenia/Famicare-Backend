package com.famicare.FamicareBackend.subscription.application.queryservices;

import com.famicare.FamicareBackend.subscription.domain.model.aggregates.Subscription;
import com.famicare.FamicareBackend.subscription.domain.model.queries.GetSubscriptionByIdQuery;
import com.famicare.FamicareBackend.subscription.domain.model.queries.GetSubscriptionByUserIdQuery;

import java.util.Optional;

public interface SubscriptionQueryService {
    Optional<Subscription> handle(GetSubscriptionByIdQuery query);
    Optional<Subscription> handle(GetSubscriptionByUserIdQuery query);
}
