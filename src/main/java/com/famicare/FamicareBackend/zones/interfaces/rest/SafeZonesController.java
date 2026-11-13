package com.famicare.FamicareBackend.zones.interfaces.rest;

import com.famicare.FamicareBackend.shared.application.result.Result;
import com.famicare.FamicareBackend.zones.application.commandservices.SafeZoneCommandFailure;
import com.famicare.FamicareBackend.zones.application.commandservices.SafeZoneCommandService;
import com.famicare.FamicareBackend.zones.application.queryservices.SafeZoneQueryService;
import com.famicare.FamicareBackend.zones.domain.model.aggregates.SafeZone;
import com.famicare.FamicareBackend.zones.domain.model.queries.GetSafeZoneByIdQuery;
import com.famicare.FamicareBackend.zones.domain.model.queries.GetSafeZonesByElderlyProfileIdQuery;
import com.famicare.FamicareBackend.zones.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.zones.interfaces.rest.resources.CreateSafeZoneResource;
import com.famicare.FamicareBackend.zones.interfaces.rest.resources.SafeZoneResource;
import com.famicare.FamicareBackend.zones.interfaces.rest.resources.UpdateSafeZoneResource;
import com.famicare.FamicareBackend.zones.interfaces.rest.transform.CreateSafeZoneCommandFromResourceAssembler;
import com.famicare.FamicareBackend.zones.interfaces.rest.transform.SafeZoneResourceFromEntityAssembler;
import com.famicare.FamicareBackend.zones.interfaces.rest.transform.UpdateSafeZoneCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Locale;

/**
 * REST controller for the safe zones (geofences) of an elderly profile: definition (US-10),
 * edition (US-11) and consultation, including where the last known coordinate stands
 * relative to each zone.
 */
@RestController
@RequestMapping(value = "/api/v1/safe-zones", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Safe Zones", description = "Safe zones (geofences) and their continuous evaluation")
public class SafeZonesController {

    private final SafeZoneCommandService safeZoneCommandService;
    private final SafeZoneQueryService safeZoneQueryService;
    private final MessageSource messageSource;

    public SafeZonesController(SafeZoneCommandService safeZoneCommandService,
                               SafeZoneQueryService safeZoneQueryService,
                               MessageSource messageSource) {
        this.safeZoneCommandService = safeZoneCommandService;
        this.safeZoneQueryService = safeZoneQueryService;
        this.messageSource = messageSource;
    }

    @Operation(summary = "Define a safe zone", description = "Creates a circular safe zone (center and radius) for an elderly profile")
    @ApiResponse(responseCode = "201", description = "Safe zone created")
    @ApiResponse(responseCode = "400", description = "Invalid zone data")
    @ApiResponse(responseCode = "409", description = "The elderly profile already has a zone with that name")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SafeZoneResource> createSafeZone(@Valid @RequestBody CreateSafeZoneResource resource,
                                                           Locale locale) {
        var command = CreateSafeZoneCommandFromResourceAssembler.toCommandFromResource(resource);
        var safeZone = unwrap(safeZoneCommandService.handle(command), locale);
        var body = SafeZoneResourceFromEntityAssembler.toResourceFromEntity(safeZone);
        return ResponseEntity.created(URI.create("/api/v1/safe-zones/" + body.id())).body(body);
    }

    @Operation(summary = "Edit a safe zone", description = "Replaces the name and the area of an existing safe zone")
    @ApiResponse(responseCode = "200", description = "Safe zone updated")
    @ApiResponse(responseCode = "400", description = "Invalid zone data")
    @ApiResponse(responseCode = "404", description = "Safe zone not found")
    @ApiResponse(responseCode = "409", description = "The elderly profile already has a zone with that name")
    @PutMapping(value = "/{safeZoneId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SafeZoneResource> updateSafeZone(@PathVariable Long safeZoneId,
                                                           @Valid @RequestBody UpdateSafeZoneResource resource,
                                                           Locale locale) {
        var command = UpdateSafeZoneCommandFromResourceAssembler.toCommandFromResource(safeZoneId, resource);
        var safeZone = unwrap(safeZoneCommandService.handle(command), locale);
        return ResponseEntity.ok(SafeZoneResourceFromEntityAssembler.toResourceFromEntity(safeZone));
    }

    @Operation(summary = "Get a safe zone by id")
    @ApiResponse(responseCode = "200", description = "Safe zone found")
    @ApiResponse(responseCode = "404", description = "Safe zone not found")
    @GetMapping("/{safeZoneId}")
    public ResponseEntity<SafeZoneResource> getSafeZoneById(@PathVariable Long safeZoneId, Locale locale) {
        var safeZone = safeZoneQueryService.handle(new GetSafeZoneByIdQuery(safeZoneId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, message("zone.error.notFound", locale)));
        return ResponseEntity.ok(SafeZoneResourceFromEntityAssembler.toResourceFromEntity(safeZone));
    }

    @Operation(summary = "Get the safe zones of an elderly profile")
    @ApiResponse(responseCode = "200", description = "Safe zones of the elderly profile (possibly empty)")
    @GetMapping(params = "elderlyProfileId")
    public ResponseEntity<List<SafeZoneResource>> getSafeZonesByElderlyProfileId(@RequestParam Long elderlyProfileId) {
        var query = new GetSafeZonesByElderlyProfileIdQuery(new ElderlyProfileId(elderlyProfileId));
        var resources = safeZoneQueryService.handle(query).stream()
                .map(SafeZoneResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    private SafeZone unwrap(Result<SafeZone, SafeZoneCommandFailure> result, Locale locale) {
        return result.fold(
                safeZone -> safeZone,
                failure -> {
                    throw new ResponseStatusException(statusOf(failure), message(failure.messageKey(), locale));
                });
    }

    private HttpStatus statusOf(SafeZoneCommandFailure failure) {
        if (failure instanceof SafeZoneCommandFailure.SafeZoneNotFound) return HttpStatus.NOT_FOUND;
        return HttpStatus.CONFLICT; // DuplicateZoneName
    }

    private String message(String key, Locale locale) {
        return messageSource.getMessage(key, null, key, locale);
    }
}
