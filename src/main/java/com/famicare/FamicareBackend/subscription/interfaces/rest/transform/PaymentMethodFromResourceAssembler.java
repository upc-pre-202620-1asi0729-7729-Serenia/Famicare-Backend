package com.famicare.FamicareBackend.subscription.interfaces.rest.transform;

import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.PaymentMethod;
import com.famicare.FamicareBackend.subscription.interfaces.rest.resources.PaymentMethodResource;

public final class PaymentMethodFromResourceAssembler {
    private PaymentMethodFromResourceAssembler() {}

    public static PaymentMethod toValueObject(PaymentMethodResource resource) {
        if (resource == null) return null;
        return new PaymentMethod(resource.token(), resource.brand(), resource.lastFour(),
                resource.expirationMonth(), resource.expirationYear());
    }
}
