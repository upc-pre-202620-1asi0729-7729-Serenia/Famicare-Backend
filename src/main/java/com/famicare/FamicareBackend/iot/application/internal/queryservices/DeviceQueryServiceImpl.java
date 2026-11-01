package com.famicare.FamicareBackend.iot.application.internal.queryservices;

import com.famicare.FamicareBackend.iot.application.queryservices.DeviceQueryService;
import com.famicare.FamicareBackend.iot.domain.model.aggregates.Device;
import com.famicare.FamicareBackend.iot.domain.model.queries.GetDeviceByIdQuery;
import com.famicare.FamicareBackend.iot.domain.model.queries.GetDevicesByElderlyProfileIdQuery;
import com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class DeviceQueryServiceImpl implements DeviceQueryService {

    private final DeviceRepository deviceRepository;

    public DeviceQueryServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Optional<Device> handle(GetDeviceByIdQuery query) {
        var result = deviceRepository.findById(query.id());
        if (result.isEmpty()) log.debug("No device found for id={}", query.id());
        return result;
    }

    @Override
    public List<Device> handle(GetDevicesByElderlyProfileIdQuery query) {
        return deviceRepository.findAllByElderlyProfileId(query.elderlyProfileId());
    }
}
