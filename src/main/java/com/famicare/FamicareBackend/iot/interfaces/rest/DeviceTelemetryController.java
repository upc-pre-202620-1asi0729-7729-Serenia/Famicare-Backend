package com.famicare.FamicareBackend.iot.interfaces.rest;

import com.famicare.FamicareBackend.iot.application.commandservices.TelemetryCommandFailure;
import com.famicare.FamicareBackend.iot.application.commandservices.TelemetryCommandService;
import com.famicare.FamicareBackend.iot.application.queryservices.DeviceQueryService;
import com.famicare.FamicareBackend.iot.application.queryservices.TelemetryEventQueryService;
import com.famicare.FamicareBackend.iot.domain.model.queries.GetDeviceByIdQuery;
import com.famicare.FamicareBackend.iot.domain.model.queries.GetTelemetryEventsByDeviceIdAndPeriodQuery;
import com.famicare.FamicareBackend.iot.interfaces.rest.resources.RecordTelemetryResource;
import com.famicare.FamicareBackend.iot.interfaces.rest.resources.TelemetryEventResource;
import com.famicare.FamicareBackend.iot.interfaces.rest.transform.RecordTelemetryCommandFromResourceAssembler;
import com.famicare.FamicareBackend.iot.interfaces.rest.transform.TelemetryEventResourceFromEntityAssembler;
import com.famicare.FamicareBackend.shared.application.result.Result;
import com.famicare.FamicareBackend.iot.domain.model.aggregates.TelemetryEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

/**
 * REST controller through which paired IoT devices report telemetry (location, heartbeat,
 * panic button, fall) and through which the stored events can be consulted.
 */
@RestController
@RequestMapping(value = "/api/v1/devices/{deviceId}/telemetry", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Device Telemetry", description = "Telemetry reported by IoT devices")
public class DeviceTelemetryController {

    private final TelemetryCommandService telemetryCommandService;
    private final TelemetryEventQueryService telemetryEventQueryService;
    private final DeviceQueryService deviceQueryService;
    private final MessageSource messageSource;

    public DeviceTelemetryController(TelemetryCommandService telemetryCommandService,
                                     TelemetryEventQueryService telemetryEventQueryService,
                                     DeviceQueryService deviceQueryService,
                                     MessageSource messageSource) {
        this.telemetryCommandService = telemetryCommandService;
        this.telemetryEventQueryService = telemetryEventQueryService;
        this.deviceQueryService = deviceQueryService;
        this.messageSource = messageSource;
    }

    @Operation(summary = "Record telemetry", description = "Stores a message sent by a paired device and refreshes its heartbeat and battery level")
    @ApiResponse(responseCode = "201", description = "Telemetry recorded")
    @ApiResponse(responseCode = "400", description = "Invalid telemetry data")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @ApiResponse(responseCode = "409", description = "Device is not paired")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TelemetryEventResource> recordTelemetry(@PathVariable Long deviceId,
                                                                  @Valid @RequestBody RecordTelemetryResource resource,
                                                                  Locale locale) {
        var command = RecordTelemetryCommandFromResourceAssembler.toCommandFromResource(deviceId, resource);
        var telemetryEvent = unwrap(telemetryCommandService.handle(command), locale);
        var body = TelemetryEventResourceFromEntityAssembler.toResourceFromEntity(telemetryEvent);
        return ResponseEntity.created(URI.create("/api/v1/devices/" + deviceId + "/telemetry/" + body.id())).body(body);
    }

    @Operation(summary = "Get telemetry history", description = "Returns the events recorded by the device between 'from' and 'to' (ISO-8601 instants, inclusive), oldest first")
    @ApiResponse(responseCode = "200", description = "Events in the period (possibly empty)")
    @ApiResponse(responseCode = "400", description = "Invalid period")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @GetMapping
    public ResponseEntity<List<TelemetryEventResource>> getTelemetryByPeriod(@PathVariable Long deviceId,
                                                                             @RequestParam Instant from,
                                                                             @RequestParam Instant to,
                                                                             Locale locale) {
        var query = new GetTelemetryEventsByDeviceIdAndPeriodQuery(deviceId, from, to);
        if (deviceQueryService.handle(new GetDeviceByIdQuery(deviceId)).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message("device.error.notFound", locale));
        }
        var resources = telemetryEventQueryService.handle(query).stream()
                .map(TelemetryEventResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    private TelemetryEvent unwrap(Result<TelemetryEvent, TelemetryCommandFailure> result, Locale locale) {
        return result.fold(
                event -> event,
                failure -> {
                    throw new ResponseStatusException(statusOf(failure), message(failure.messageKey(), locale));
                });
    }

    private HttpStatus statusOf(TelemetryCommandFailure failure) {
        if (failure instanceof TelemetryCommandFailure.DeviceNotFound) return HttpStatus.NOT_FOUND;
        return HttpStatus.CONFLICT; // DeviceNotPaired
    }

    private String message(String key, Locale locale) {
        return messageSource.getMessage(key, null, key, locale);
    }
}
