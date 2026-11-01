package com.famicare.FamicareBackend.iot.domain.model.queries;

public record GetDeviceByIdQuery(Long id) {
    public GetDeviceByIdQuery {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("device.error.id.invalid");
        }
    }
}
