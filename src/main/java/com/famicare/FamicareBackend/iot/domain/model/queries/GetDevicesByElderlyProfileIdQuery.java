package com.famicare.FamicareBackend.iot.domain.model.queries;

import com.famicare.FamicareBackend.iot.domain.model.valueobjects.ElderlyProfileId;

public record GetDevicesByElderlyProfileIdQuery(ElderlyProfileId elderlyProfileId) {
    public GetDevicesByElderlyProfileIdQuery {
        if (elderlyProfileId == null)
            throw new IllegalArgumentException("elderlyProfileId cannot be null");
    }
}
