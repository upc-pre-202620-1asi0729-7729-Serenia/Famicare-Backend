package com.famicare.FamicareBackend.care.application.commandservices;

import com.famicare.FamicareBackend.care.domain.model.aggregates.Caregiver;
import com.famicare.FamicareBackend.care.domain.model.commands.AcceptCaregiverInvitationCommand;
import com.famicare.FamicareBackend.care.domain.model.commands.InviteCaregiverCommand;
import com.famicare.FamicareBackend.care.domain.model.commands.RevokeCaregiverAccessCommand;
import com.famicare.FamicareBackend.shared.application.result.Result;

public interface CaregiverCommandService {
    Result<Caregiver, CareCommandFailure> handle(InviteCaregiverCommand command);
    Result<Caregiver, CareCommandFailure> handle(AcceptCaregiverInvitationCommand command);
    Result<Caregiver, CareCommandFailure> handle(RevokeCaregiverAccessCommand command);
}