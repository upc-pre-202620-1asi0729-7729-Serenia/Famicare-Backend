package com.famicare.FamicareBackend.subscription.domain.model.valueobjects;

import java.math.BigDecimal;

public enum PlanType {
    FREE("Free", new BigDecimal("0.00")),
    FAMILIAR_PLUS("Familiar Plus", new BigDecimal("18.99")),
    FAMILIAR_PREMIUM("Familiar Premium", new BigDecimal("29.99"));

    private final String displayName;
    private final BigDecimal monthlyPrice;

    PlanType(String displayName, BigDecimal monthlyPrice) {
        this.displayName = displayName;
        this.monthlyPrice = monthlyPrice;
    }

    public String displayName() {
        return displayName;
    }

    public BigDecimal monthlyPrice() {
        return monthlyPrice;
    }

    public boolean isPaid() {
        return this != FREE;
    }
}
