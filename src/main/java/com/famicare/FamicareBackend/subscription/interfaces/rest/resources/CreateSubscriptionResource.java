package com.famicare.FamicareBackend.subscription.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateSubscriptionResource(
        @NotNull(message = "{subscription.error.userId.invalid}")
        @Positive(message = "{subscription.error.userId.invalid}")
        Long userId,
        @Schema(example = "FAMILIAR_PREMIUM", allowableValues = {"FREE", "FAMILIAR_PLUS", "FAMILIAR_PREMIUM"})
        @NotBlank(message = "{subscription.error.plan.invalid}")
        String planType,
        @Valid PaymentMethodResource paymentMethod
) {}
