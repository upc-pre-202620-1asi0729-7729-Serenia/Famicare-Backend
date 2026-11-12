package com.famicare.FamicareBackend.subscription.interfaces.rest;

import com.famicare.FamicareBackend.shared.application.result.Result;
import com.famicare.FamicareBackend.subscription.application.commandservices.SubscriptionCommandFailure;
import com.famicare.FamicareBackend.subscription.application.commandservices.SubscriptionCommandService;
import com.famicare.FamicareBackend.subscription.application.queryservices.SubscriptionQueryService;
import com.famicare.FamicareBackend.subscription.domain.model.aggregates.Subscription;
import com.famicare.FamicareBackend.subscription.domain.model.commands.CancelSubscriptionCommand;
import com.famicare.FamicareBackend.subscription.domain.model.commands.ChangePlanCommand;
import com.famicare.FamicareBackend.subscription.domain.model.commands.UpdatePaymentMethodCommand;
import com.famicare.FamicareBackend.subscription.domain.model.queries.GetSubscriptionByIdQuery;
import com.famicare.FamicareBackend.subscription.domain.model.queries.GetSubscriptionByUserIdQuery;
import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.PlanType;
import com.famicare.FamicareBackend.subscription.domain.model.valueobjects.UserId;
import com.famicare.FamicareBackend.subscription.interfaces.rest.resources.*;
import com.famicare.FamicareBackend.subscription.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Subscriptions", description = "Subscription plan and billing lifecycle")
public class SubscriptionsController {
    private final SubscriptionCommandService commandService;
    private final SubscriptionQueryService queryService;
    private final MessageSource messageSource;

    public SubscriptionsController(SubscriptionCommandService commandService,
                                   SubscriptionQueryService queryService,
                                   MessageSource messageSource) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.messageSource = messageSource;
    }

    @Operation(summary = "List available plans")
    @GetMapping("/plans")
    public List<PlanResource> getPlans() {
        return Arrays.stream(PlanType.values())
                .map(plan -> new PlanResource(plan.name(), plan.displayName(), plan.monthlyPrice(), "PEN"))
                .toList();
    }

    @Operation(summary = "Create a subscription")
    @ApiResponse(responseCode = "201", description = "Subscription created")
    @ApiResponse(responseCode = "409", description = "The user already has a subscription")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubscriptionResource> create(
            @Valid @RequestBody CreateSubscriptionResource resource, Locale locale) {
        var subscription = unwrap(commandService.handle(
                CreateSubscriptionCommandFromResourceAssembler.toCommand(resource)), locale);
        var body = SubscriptionResourceFromEntityAssembler.toResource(subscription);
        return ResponseEntity.created(URI.create("/api/v1/subscriptions/" + body.id())).body(body);
    }

    @Operation(summary = "Get a subscription by id")
    @GetMapping("/{subscriptionId}")
    public SubscriptionResource getById(@PathVariable Long subscriptionId, Locale locale) {
        var subscription = queryService.handle(new GetSubscriptionByIdQuery(subscriptionId))
                .orElseThrow(() -> notFound(locale));
        return SubscriptionResourceFromEntityAssembler.toResource(subscription);
    }

    @Operation(summary = "Get the subscription of a user")
    @GetMapping(params = "userId")
    public SubscriptionResource getByUserId(@RequestParam Long userId, Locale locale) {
        var subscription = queryService.handle(new GetSubscriptionByUserIdQuery(new UserId(userId)))
                .orElseThrow(() -> notFound(locale));
        return SubscriptionResourceFromEntityAssembler.toResource(subscription);
    }

    @Operation(summary = "Change the current plan")
    @PatchMapping(value = "/{subscriptionId}/plan", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SubscriptionResource changePlan(@PathVariable Long subscriptionId,
                                           @Valid @RequestBody ChangePlanResource resource,
                                           Locale locale) {
        var command = new ChangePlanCommand(subscriptionId,
                SubscriptionValueObjectAssembler.toPlanType(resource.planType()));
        return SubscriptionResourceFromEntityAssembler.toResource(
                unwrap(commandService.handle(command), locale));
    }

    @Operation(summary = "Replace the tokenized payment method")
    @PatchMapping(value = "/{subscriptionId}/payment-method", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SubscriptionResource updatePaymentMethod(@PathVariable Long subscriptionId,
                                                     @Valid @RequestBody PaymentMethodResource resource,
                                                     Locale locale) {
        var command = new UpdatePaymentMethodCommand(subscriptionId,
                PaymentMethodFromResourceAssembler.toValueObject(resource));
        return SubscriptionResourceFromEntityAssembler.toResource(
                unwrap(commandService.handle(command), locale));
    }

    @Operation(summary = "Cancel at the end of the current billing period")
    @PostMapping("/{subscriptionId}/cancel")
    public SubscriptionResource cancel(@PathVariable Long subscriptionId, Locale locale) {
        return SubscriptionResourceFromEntityAssembler.toResource(
                unwrap(commandService.handle(new CancelSubscriptionCommand(subscriptionId)), locale));
    }

    private Subscription unwrap(Result<Subscription, SubscriptionCommandFailure> result, Locale locale) {
        return result.fold(value -> value, failure -> {
            throw new ResponseStatusException(statusOf(failure), message(failure.messageKey(), locale));
        });
    }

    private HttpStatus statusOf(SubscriptionCommandFailure failure) {
        if (failure instanceof SubscriptionCommandFailure.SubscriptionNotFound) return HttpStatus.NOT_FOUND;
        if (failure instanceof SubscriptionCommandFailure.SubscriptionAlreadyExists
                || failure instanceof SubscriptionCommandFailure.CancellationAlreadyScheduled)
            return HttpStatus.CONFLICT;
        return HttpStatus.UNPROCESSABLE_CONTENT;
    }

    private ResponseStatusException notFound(Locale locale) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND,
                message("subscription.error.notFound", locale));
    }

    private String message(String key, Locale locale) {
        return messageSource.getMessage(key, null, key, locale);
    }
}
