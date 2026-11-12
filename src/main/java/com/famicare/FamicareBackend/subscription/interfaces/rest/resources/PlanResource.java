package com.famicare.FamicareBackend.subscription.interfaces.rest.resources;

import java.math.BigDecimal;

public record PlanResource(String code, String displayName, BigDecimal priceMonthly, String currency) {}
