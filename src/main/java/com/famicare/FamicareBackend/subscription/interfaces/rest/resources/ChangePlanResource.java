package com.famicare.FamicareBackend.subscription.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record ChangePlanResource(
        @NotBlank(message = "{subscription.error.plan.invalid}") String planType
) {}
