package com.famicare.FamicareBackend.monitor.interfaces.rest;

import com.famicare.FamicareBackend.monitor.application.TrackingService;
import com.famicare.FamicareBackend.monitor.domain.model.queries.TrackingQueries.*;
import com.famicare.FamicareBackend.monitor.interfaces.rest.resources.TrackingResources.*;
import com.famicare.FamicareBackend.monitor.interfaces.rest.transform.TrackingAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/v1/monitor/tracking")
@Tag(name = "Tracking and telemetry", description = "Endpoint de ubicacion GPS y telemetria")
public class TrackingController {

    private final TrackingService trackingService;

    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @PostMapping("/locations")
    public ResponseEntity<LocationRecordResource> recordLocation(@RequestBody RecordLocationResource resource) {
        var record = trackingService.handle(TrackingAssembler.toCommand(resource));
        return new ResponseEntity<>(TrackingAssembler.toResource(record), HttpStatus.CREATED);
    }

    @GetMapping("/locations/latest")
    public ResponseEntity<LocationRecordResource> getLatestLocation(@RequestParam Long elderlyProfileId) {
        return trackingService.handle(new GetLatestLocationQuery(elderlyProfileId))
                .map(record -> ResponseEntity.ok(TrackingAssembler.toResource(record)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/locations/history")
    public ResponseEntity<List<LocationRecordResource>> getLocationHistory(@RequestParam Long elderlyProfileId) {
        var list = trackingService.handle(new GetLocationHistoryQuery(elderlyProfileId))
                .stream().map(TrackingAssembler::toResource).toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/telemetry")
    public ResponseEntity<TelemetryEventResource> reportTelemetry(@RequestBody RecordTelemetryEventResource resource) {
        var event = trackingService.handle(TrackingAssembler.toCommand(resource));
        return new ResponseEntity<>(TrackingAssembler.toResource(event), HttpStatus.CREATED);
    }

    @GetMapping("/telemetry")
    public ResponseEntity<List<TelemetryEventResource>> getTelemetryEvents(@RequestParam Long elderlyProfileId) {
        var list = trackingService.handle(new GetTelemetryEventsQuery(elderlyProfileId))
                .stream().map(TrackingAssembler::toResource).toList();
        return ResponseEntity.ok(list);
    }
}