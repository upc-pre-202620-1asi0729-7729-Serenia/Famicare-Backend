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
import com.famicare.FamicareBackend.monitor.interfaces.rest.transform.AlertResourceFromEntityAssembler;
import com.famicare.FamicareBackend.monitor.interfaces.rest.transform.AttendAlertCommandFromResourceAssembler;
import com.famicare.FamicareBackend.monitor.interfaces.rest.transform.CreateAlertCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/monitor")
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
        var command = CreateAlertCommandFromResourceAssembler.toCommandFromResource(resource);
        var alert = alertCommandService.handle(command);
        return alert.map(a -> new ResponseEntity<>(AlertResourceFromEntityAssembler.toResourceFromEntity(a), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{alertId}")
    public ResponseEntity<AlertResource> getAlertById(@PathVariable Long alertId) {
        var query = new GetAlertByIdQuery(alertId);
        var alert = alertQueryService.handle(query);
        return alert.map(a -> ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceFromEntity(a)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AlertResource>> getAlerts(
            @RequestParam Long elderlyProfileId,
            @RequestParam(required = false) String status) {

        var profileId = new ElderlyProfileId(elderlyProfileId);
        List<AlertResource> resources;

        if (status != null && !status.isBlank()) {
            var alertStatus = AlertStatus.valueOf(status.toUpperCase());
            var query = new GetAlertsByElderlyProfileIdAndStatusQuery(profileId, alertStatus);
            resources = alertQueryService.handle(query)
                    .stream()
                    .map(AlertResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
        } else {
            var query = new GetAlertsByElderlyProfileIdQuery(profileId);
            resources = alertQueryService.handle(query)
                    .stream()
                    .map(AlertResourceFromEntityAssembler::toResourceFromEntity)
                    .toList();
        }

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{alertId}/attend")
    public ResponseEntity<AlertResource> attendAlert(
            @PathVariable Long alertId,
            @RequestBody AttendAlertResource resource) {
        var command = AttendAlertCommandFromResourceAssembler.toCommandFromResource(alertId, resource);
        var alert = alertCommandService.handle(command);
        return alert.map(a -> ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceFromEntity(a)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{alertId}/resolve")
    public ResponseEntity<AlertResource> resolveAlert(@PathVariable Long alertId) {
        var command = new ResolveAlertCommand(alertId);
        var alert = alertCommandService.handle(command);
        return alert.map(a -> ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceFromEntity(a)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}