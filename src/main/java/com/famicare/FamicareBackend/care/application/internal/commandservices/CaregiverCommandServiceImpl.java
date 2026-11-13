package com.famicare.FamicareBackend.care.application.internal.commandservices;

import com.famicare.FamicareBackend.care.application.commandservices.CareCommandFailure;
import com.famicare.FamicareBackend.care.application.commandservices.CaregiverCommandService;
import com.famicare.FamicareBackend.care.domain.model.aggregates.Caregiver;
import com.famicare.FamicareBackend.care.domain.model.commands.AcceptCaregiverInvitationCommand;
import com.famicare.FamicareBackend.care.domain.model.commands.InviteCaregiverCommand;
import com.famicare.FamicareBackend.care.domain.model.commands.RevokeCaregiverAccessCommand;
import com.famicare.FamicareBackend.care.domain.model.events.CaregiverAccessRevokedEvent;
import com.famicare.FamicareBackend.care.domain.model.events.CaregiverInvitedEvent;
import com.famicare.FamicareBackend.care.domain.model.valueobjects.InvitationStatus;
import com.famicare.FamicareBackend.care.infrastructure.persistence.jpa.CaregiverRepository;
import com.famicare.FamicareBackend.shared.application.result.Result;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CaregiverCommandServiceImpl implements CaregiverCommandService {

    private final CaregiverRepository caregiverRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CaregiverCommandServiceImpl(CaregiverRepository caregiverRepository,
                                       ApplicationEventPublisher eventPublisher) {
        this.caregiverRepository = caregiverRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Result<Caregiver, CareCommandFailure> handle(InviteCaregiverCommand command) {
        String normalizedEmail = command.email().trim().toLowerCase();
        if (caregiverRepository.existsByElderlyProfileIdAndEmailValueAndStatusNot(
                command.elderlyProfileId(), normalizedEmail, InvitationStatus.REVOKED)) {
            return Result.fallido(new CareCommandFailure.CaregiverAlreadyInvited());
        }

        var caregiver = caregiverRepository.save(new Caregiver(command));
        eventPublisher.publishEvent(new CaregiverInvitedEvent(
                caregiver.getId(), caregiver.getElderlyProfileId(), caregiver.getEmail().value(), Instant.now()));
        return Result.logrado(caregiver);
    }

    @Override
    @Transactional
    public Result<Caregiver, CareCommandFailure> handle(AcceptCaregiverInvitationCommand command) {
        var optionalCaregiver = caregiverRepository.findById(command.caregiverId());
        if (optionalCaregiver.isEmpty()) {
            return Result.fallido(new CareCommandFailure.CaregiverNotFound());
        }

        var caregiver = optionalCaregiver.get();
        if (!caregiver.acceptInvitation()) {
            return Result.fallido(new CareCommandFailure.InvalidInvitationState());
        }

        return Result.logrado(caregiverRepository.save(caregiver));
    }

    @Override
    @Transactional
    public Result<Caregiver, CareCommandFailure> handle(RevokeCaregiverAccessCommand command) {
        var optionalCaregiver = caregiverRepository.findById(command.caregiverId());
        if (optionalCaregiver.isEmpty()) {
            return Result.fallido(new CareCommandFailure.CaregiverNotFound());
        }

        var caregiver = optionalCaregiver.get();
        if (!caregiver.revokeAccess()) {
            return Result.fallido(new CareCommandFailure.InvalidInvitationState());
        }

        var saved = caregiverRepository.save(caregiver);
        eventPublisher.publishEvent(new CaregiverAccessRevokedEvent(
                saved.getId(), saved.getElderlyProfileId(), Instant.now()));
        return Result.logrado(saved);
    }
}