package com.famicare.FamicareBackend.iot.application.queryservices;

import com.famicare.FamicareBackend.iot.domain.model.aggregates.Device;
import com.famicare.FamicareBackend.iot.domain.model.queries.GetDeviceByIdQuery;
import com.famicare.FamicareBackend.iot.domain.model.queries.GetDevicesByElderlyProfileIdQuery;

import java.util.List;
import java.util.Optional;

public interface DeviceQueryService {
    Optional<Device> handle(GetDeviceByIdQuery query);
    List<Device> handle(GetDevicesByElderlyProfileIdQuery query);
}
