package com.famicare.FamicareBackend.iot.interfaces.rest;

import com.famicare.FamicareBackend.iot.application.commandservices.DeviceCommandFailure;
import com.famicare.FamicareBackend.iot.application.commandservices.DeviceCommandService;
import com.famicare.FamicareBackend.iot.application.queryservices.DeviceQueryService;
import com.famicare.FamicareBackend.iot.domain.model.aggregates.Device;
import com.famicare.FamicareBackend.iot.domain.model.queries.GetDeviceByIdQuery;
import com.famicare.FamicareBackend.iot.domain.model.queries.GetDevicesByElderlyProfileIdQuery;
import com.famicare.FamicareBackend.iot.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.iot.interfaces.rest.resources.DeviceResource;
import com.famicare.FamicareBackend.iot.interfaces.rest.resources.PairDeviceResource;
import com.famicare.FamicareBackend.iot.interfaces.rest.resources.RegisterDeviceResource;
import com.famicare.FamicareBackend.iot.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import com.famicare.FamicareBackend.iot.interfaces.rest.transform.PairDeviceCommandFromResourceAssembler;
import com.famicare.FamicareBackend.iot.interfaces.rest.transform.RegisterDeviceCommandFromResourceAssembler;
import com.famicare.FamicareBackend.shared.application.result.Result;
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
import java.util.List;
import java.util.Locale;

/**
 * REST controller for the IoT device lifecycle: registration, pairing (US-07)
 * and consultation of the device state and battery level (US-08).
 */
@RestController
@RequestMapping(value = "/api/v1/devices", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Devices", description = "IoT device management")
public class DevicesController {

    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;
    private final MessageSource messageSource;

    public DevicesController(DeviceCommandService deviceCommandService,
                             DeviceQueryService deviceQueryService,
                             MessageSource messageSource) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
        this.messageSource = messageSource;
    }

    @Operation(summary = "Register a device", description = "Registers a device in PENDING status using its pairing code")
    @ApiResponse(responseCode = "201", description = "Device registered")
    @ApiResponse(responseCode = "400", description = "Invalid pairing code")
    @ApiResponse(responseCode = "409", description = "Pairing code already registered")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResource> registerDevice(@Valid @RequestBody RegisterDeviceResource resource,
                                                         Locale locale) {
        var command = RegisterDeviceCommandFromResourceAssembler.toCommandFromResource(resource);
        var device = unwrap(deviceCommandService.handle(command), locale);
        var body = DeviceResourceFromEntityAssembler.toResourceFromEntity(device);
        return ResponseEntity.created(URI.create("/api/v1/devices/" + body.id())).body(body);
    }

    @Operation(summary = "Pair a device", description = "Links a registered device to an elderly profile using its pairing code")
    @ApiResponse(responseCode = "200", description = "Device paired")
    @ApiResponse(responseCode = "404", description = "No device with that pairing code")
    @ApiResponse(responseCode = "409", description = "Device already paired")
    @PostMapping(value = "/pair", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResource> pairDevice(@Valid @RequestBody PairDeviceResource resource,
                                                     Locale locale) {
        var command = PairDeviceCommandFromResourceAssembler.toCommandFromResource(resource);
        var device = unwrap(deviceCommandService.handle(command), locale);
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Get a device by id", description = "Returns the device status, battery level and online state")
    @ApiResponse(responseCode = "200", description = "Device found")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceResource> getDeviceById(@PathVariable Long deviceId, Locale locale) {
        var device = deviceQueryService.handle(new GetDeviceByIdQuery(deviceId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, message("device.error.notFound", locale)));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Get the devices of an elderly profile")
    @ApiResponse(responseCode = "200", description = "Devices linked to the elderly profile (possibly empty)")
    @GetMapping(params = "elderlyProfileId")
    public ResponseEntity<List<DeviceResource>> getDevicesByElderlyProfileId(@RequestParam Long elderlyProfileId) {
        var query = new GetDevicesByElderlyProfileIdQuery(new ElderlyProfileId(elderlyProfileId));
        var resources = deviceQueryService.handle(query).stream()
                .map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    private Device unwrap(Result<Device, DeviceCommandFailure> result, Locale locale) {
        return result.fold(
                device -> device,
                failure -> {
                    throw new ResponseStatusException(statusOf(failure), message(failure.messageKey(), locale));
                });
    }

    private HttpStatus statusOf(DeviceCommandFailure failure) {
        if (failure instanceof DeviceCommandFailure.PairingCodeNotFound) return HttpStatus.NOT_FOUND;
        return HttpStatus.CONFLICT; // DuplicatePairingCode, AlreadyPaired
    }

    private String message(String key, Locale locale) {
        return messageSource.getMessage(key, null, key, locale);
    }
}
