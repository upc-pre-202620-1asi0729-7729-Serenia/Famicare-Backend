package com.famicare.FamicareBackend.care.interfaces.rest;

import com.famicare.FamicareBackend.care.application.commandservices.CareCommandFailure;
import com.famicare.FamicareBackend.care.application.commandservices.CaregiverCommandService;
import com.famicare.FamicareBackend.care.application.queryservices.CaregiverQueryService;
import com.famicare.FamicareBackend.care.domain.model.aggregates.Caregiver;
import com.famicare.FamicareBackend.care.domain.model.commands.AcceptCaregiverInvitationCommand;
import com.famicare.FamicareBackend.care.domain.model.commands.RevokeCaregiverAccessCommand;
import com.famicare.FamicareBackend.care.domain.model.queries.GetCaregiversByElderlyProfileIdQuery;
import com.famicare.FamicareBackend.care.interfaces.rest.resources.CaregiverResource;
import com.famicare.FamicareBackend.care.interfaces.rest.resources.InviteCaregiverResource;
import com.famicare.FamicareBackend.care.interfaces.rest.transform.CaregiverResourceFromEntityAssembler;
import com.famicare.FamicareBackend.care.interfaces.rest.transform.InviteCaregiverCommandFromResourceAssembler;
import com.famicare.FamicareBackend.shared.application.result.Result;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping(value = "/api/v1/care-network", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Care Network")
public class CareNetworkController {

    private final CaregiverCommandService commandService;
    private final CaregiverQueryService queryService;
    private final MessageSource messageSource;

    public CareNetworkController(CaregiverCommandService commandService,
                                 CaregiverQueryService queryService,
                                 MessageSource messageSource) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<List<CaregiverResource>> getByElderlyProfileId(
            @RequestParam(defaultValue = "1") Long elderlyProfileId) {
        var caregivers = queryService.handle(new GetCaregiversByElderlyProfileIdQuery(elderlyProfileId)).stream()
                .map(CaregiverResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(caregivers);
    }

    @PostMapping(value = "/invite", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CaregiverResource> invite(
            @Valid @RequestBody InviteCaregiverResource resource, Locale locale) {
        var command = InviteCaregiverCommandFromResourceAssembler.toCommandFromResource(resource);
        var caregiver = unwrap(commandService.handle(command), locale);
        var body = CaregiverResourceFromEntityAssembler.toResourceFromEntity(caregiver);
        return ResponseEntity.created(URI.create("/api/v1/care-network/" + body.id())).body(body);
    }

    @PatchMapping("/{caregiverId}/accept")
    public ResponseEntity<CaregiverResource> acceptInvitation(@PathVariable Long caregiverId, Locale locale) {
        var caregiver = unwrap(commandService.handle(new AcceptCaregiverInvitationCommand(caregiverId)), locale);
        return ResponseEntity.ok(CaregiverResourceFromEntityAssembler.toResourceFromEntity(caregiver));
    }

    @DeleteMapping("/{caregiverId}")
    public ResponseEntity<Void> revokeAccess(@PathVariable Long caregiverId, Locale locale) {
        unwrap(commandService.handle(new RevokeCaregiverAccessCommand(caregiverId)), locale);
        return ResponseEntity.noContent().build();
    }

    private Caregiver unwrap(Result<Caregiver, CareCommandFailure> result, Locale locale) {
        return result.fold(
                caregiver -> caregiver,
                failure -> {
                    throw new ResponseStatusException(statusOf(failure), message(failure.messageKey(), locale));
                });
    }

    private HttpStatus statusOf(CareCommandFailure failure) {
        if (failure instanceof CareCommandFailure.CaregiverNotFound
                || failure instanceof CareCommandFailure.ElderlyProfileNotFound) {
            return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.CONFLICT;
    }

    private String message(String key, Locale locale) {
        return messageSource.getMessage(key, null, key, locale);
    }
}