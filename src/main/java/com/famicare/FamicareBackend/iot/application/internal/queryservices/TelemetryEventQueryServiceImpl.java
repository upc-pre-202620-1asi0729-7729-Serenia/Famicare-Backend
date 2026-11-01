package com.famicare.FamicareBackend.iot.application.internal.queryservices;

import com.famicare.FamicareBackend.iot.application.queryservices.TelemetryEventQueryService;
import com.famicare.FamicareBackend.iot.domain.model.aggregates.TelemetryEvent;
import com.famicare.FamicareBackend.iot.domain.model.queries.GetTelemetryEventsByDeviceIdAndPeriodQuery;
import com.famicare.FamicareBackend.iot.infrastructure.persistence.jpa.TelemetryEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TelemetryEventQueryServiceImpl implements TelemetryEventQueryService {

    private final TelemetryEventRepository telemetryEventRepository;

    public TelemetryEventQueryServiceImpl(TelemetryEventRepository telemetryEventRepository) {
        this.telemetryEventRepository = telemetryEventRepository;
    }

    @Override
    public List<TelemetryEvent> handle(GetTelemetryEventsByDeviceIdAndPeriodQuery query) {
        return telemetryEventRepository.findAllByDeviceIdAndRecordedAtBetweenOrderByRecordedAtAsc(
                query.deviceId(), query.from(), query.to());
    }
}
