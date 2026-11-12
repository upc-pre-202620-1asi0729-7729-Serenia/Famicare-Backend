package com.famicare.FamicareBackend.monitor.interfaces.rest;

import com.famicare.FamicareBackend.monitor.application.commandservices.AlertCommandService;
import com.famicare.FamicareBackend.monitor.application.queryservices.AlertQueryService;
import com.famicare.FamicareBackend.monitor.domain.model.commands.ResolveAlertCommand;
import com.famicare.FamicareBackend.monitor.domain.model.queries.GetAlertByIdQuery;
import com.famicare.FamicareBackend.monitor.domain.model.queries.GetAlertsByElderlyProfileIdAndStatusQuery;
import com.famicare.FamicareBackend.monitor.domain.model.queries.GetAlertsByElderlyProfileIdQuery;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.AlertStatus;
import com.famicare.FamicareBackend.monitor.domain.model.valueobjects.ElderlyProfileId;
import com.famicare.FamicareBackend.monitor.interfaces.rest.resources.AlertResource;
import com.famicare.FamicareBackend.monitor.interfaces.rest.resources.AttendAlertResource;
import com.famicare.FamicareBackend.monitor.interfaces.rest.resources.CreateAlertResource;
import com.famicare.FamicareBackend.monitor.interfaces.rest.transform.AlertAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
@Tag(name = "Alerts", description = "Alerts endpoints")
public class AlertsController {

    private final AlertCommandService alertCommandService;
    private final AlertQueryService alertQueryService;

    public AlertsController(AlertCommandService alertCommandService, AlertQueryService alertQueryService) {
        this.alertCommandService = alertCommandService;
        this.alertQueryService = alertQueryService;
    }

    @PostMapping
    public ResponseEntity<AlertResource> createAlert(@RequestBody CreateAlertResource resource) {
        var command = AlertAssembler.toCommandFromResource(resource);
        var alert = alertCommandService.handle(command);
        return alert.map(a -> new ResponseEntity<>(AlertAssembler.toResourceFromEntity(a), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertResource> getAlertById(@PathVariable Long id) {
        var query = new GetAlertByIdQuery(id);
        return alertQueryService.handle(query)
                .map(a -> ResponseEntity.ok(AlertAssembler.toResourceFromEntity(a)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AlertResource>> getAlerts(
            @RequestParam Long elderlyProfileId,
            @RequestParam(required = false) String status) {

        List<AlertResource> resources;
        if (status != null && !status.isBlank()) {
            var query = new GetAlertsByElderlyProfileIdAndStatusQuery(
                    new ElderlyProfileId(elderlyProfileId),
                    AlertStatus.valueOf(status.toUpperCase())
            );
            resources = alertQueryService.handle(query).stream()
                    .map(AlertAssembler::toResourceFromEntity)
                    .toList();
        } else {
            var query = new GetAlertsByElderlyProfileIdQuery(
                    new ElderlyProfileId(elderlyProfileId)
            );
            resources = alertQueryService.handle(query).stream()
                    .map(AlertAssembler::toResourceFromEntity)
                    .toList();
        }

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{id}/attend")
    public ResponseEntity<AlertResource> attendAlert(
            @PathVariable Long id,
            @RequestBody(required = false) AttendAlertResource resource) {
        var command = AlertAssembler.toCommandFromResource(id, resource);
        var alert = alertCommandService.handle(command);
        return alert.map(a -> ResponseEntity.ok(AlertAssembler.toResourceFromEntity(a)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<AlertResource> resolveAlert(@PathVariable Long id) {
        var command = new ResolveAlertCommand(id);
        var alert = alertCommandService.handle(command);
        return alert.map(a -> ResponseEntity.ok(AlertAssembler.toResourceFromEntity(a)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}