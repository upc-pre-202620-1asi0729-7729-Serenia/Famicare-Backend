package com.famicare.FamicareBackend.subscription.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.YearMonth;

@Getter
@Embeddable
public class PaymentMethod {
    @Column(name = "payment_method_token", length = 255)
    private String token;

    @Column(name = "card_brand", length = 30)
    private String brand;

    @Column(name = "card_last_four", length = 4)
    private String lastFour;

    @Column(name = "card_exp_month")
    private Integer expirationMonth;

    @Column(name = "card_exp_year")
    private Integer expirationYear;

    protected PaymentMethod() {}

    public PaymentMethod(String token, String brand, String lastFour,
                         Integer expirationMonth, Integer expirationYear) {
        if (token == null || token.isBlank())
            throw new IllegalArgumentException("subscription.error.paymentMethod.token.required");
        if (brand == null || brand.isBlank())
            throw new IllegalArgumentException("subscription.error.paymentMethod.brand.required");
        if (lastFour == null || !lastFour.matches("\\d{4}"))
            throw new IllegalArgumentException("subscription.error.paymentMethod.lastFour.invalid");
        if (expirationMonth == null || expirationMonth < 1 || expirationMonth > 12)
            throw new IllegalArgumentException("subscription.error.paymentMethod.expiration.invalid");
        if (expirationYear == null || expirationYear < YearMonth.now().getYear())
            throw new IllegalArgumentException("subscription.error.paymentMethod.expiration.invalid");
        if (YearMonth.of(expirationYear, expirationMonth).isBefore(YearMonth.now()))
            throw new IllegalArgumentException("subscription.error.paymentMethod.expiration.invalid");

        this.token = token.trim();
        this.brand = brand.trim().toUpperCase();
        this.lastFour = lastFour;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }
}
