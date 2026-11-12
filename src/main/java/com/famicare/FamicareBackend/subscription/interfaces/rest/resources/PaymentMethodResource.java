package com.famicare.FamicareBackend.subscription.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record PaymentMethodResource(
        @Schema(description = "Token created by the payment provider; never send card number or CVV")
        @NotBlank(message = "{subscription.error.paymentMethod.token.required}")
        @Size(max = 255)
        String token,
        @NotBlank(message = "{subscription.error.paymentMethod.brand.required}")
        @Size(max = 30)
        String brand,
        @NotBlank(message = "{subscription.error.paymentMethod.lastFour.invalid}")
        @Pattern(regexp = "\\d{4}", message = "{subscription.error.paymentMethod.lastFour.invalid}")
        String lastFour,
        @NotNull(message = "{subscription.error.paymentMethod.expiration.invalid}")
        @Min(value = 1, message = "{subscription.error.paymentMethod.expiration.invalid}")
        @Max(value = 12, message = "{subscription.error.paymentMethod.expiration.invalid}")
        Integer expirationMonth,
        @NotNull(message = "{subscription.error.paymentMethod.expiration.invalid}")
        @Positive(message = "{subscription.error.paymentMethod.expiration.invalid}")
        Integer expirationYear
) {}
