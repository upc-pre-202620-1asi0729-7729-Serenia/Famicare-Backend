package com.famicare.FamicareBackend.care.interfaces.rest;

import com.famicare.FamicareBackend.care.application.commandservices.CareCommandFailure;
import com.famicare.FamicareBackend.care.application.commandservices.ElderlyProfileCommandService;
import com.famicare.FamicareBackend.care.application.queryservices.ElderlyProfileQueryService;
import com.famicare.FamicareBackend.care.domain.model.aggregates.ElderlyProfile;
import com.famicare.FamicareBackend.care.domain.model.queries.GetAllElderlyProfilesQuery;
import com.famicare.FamicareBackend.care.domain.model.queries.GetElderlyProfileByIdQuery;
import com.famicare.FamicareBackend.care.interfaces.rest.resources.CreateElderlyProfileResource;
import com.famicare.FamicareBackend.care.interfaces.rest.resources.ElderlyProfileResource;
import com.famicare.FamicareBackend.care.interfaces.rest.transform.CreateElderlyProfileCommandFromResourceAssembler;
import com.famicare.FamicareBackend.care.interfaces.rest.transform.ElderlyProfileResourceFromEntityAssembler;
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
@RequestMapping(value = "/api/v1/elderly-profiles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Elderly Profiles")
public class ElderlyProfilesController {

    private final ElderlyProfileCommandService commandService;
    private final ElderlyProfileQueryService queryService;
    private final MessageSource messageSource;

    public ElderlyProfilesController(ElderlyProfileCommandService commandService,
                                     ElderlyProfileQueryService queryService,
                                     MessageSource messageSource) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.messageSource = messageSource;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ElderlyProfileResource> create(
            @Valid @RequestBody CreateElderlyProfileResource resource, Locale locale) {
        var command = CreateElderlyProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var profile = unwrap(commandService.handle(command), locale);
        var body = ElderlyProfileResourceFromEntityAssembler.toResourceFromEntity(profile);
        return ResponseEntity.created(URI.create("/api/v1/elderly-profiles/" + body.id())).body(body);
    }

    @GetMapping
    public ResponseEntity<List<ElderlyProfileResource>> getAll() {
        var list = queryService.handle(new GetAllElderlyProfilesQuery()).stream()
                .map(ElderlyProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{elderlyProfileId}")
    public ResponseEntity<ElderlyProfileResource> getById(@PathVariable Long elderlyProfileId, Locale locale) {
        var profile = queryService.handle(new GetElderlyProfileByIdQuery(elderlyProfileId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, message("care.error.elderlyProfile.notFound", locale)));
        return ResponseEntity.ok(ElderlyProfileResourceFromEntityAssembler.toResourceFromEntity(profile));
    }

    private ElderlyProfile unwrap(Result<ElderlyProfile, CareCommandFailure> result, Locale locale) {
        return result.fold(
                profile -> profile,
                failure -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message(failure.messageKey(), locale));
                });
    }

    private String message(String key, Locale locale) {
        return messageSource.getMessage(key, null, key, locale);
    }
}