package com.famicare.FamicareBackend.care.application.commandservices;

import com.famicare.FamicareBackend.care.domain.model.aggregates.ElderlyProfile;
import com.famicare.FamicareBackend.care.domain.model.commands.CreateElderlyProfileCommand;
import com.famicare.FamicareBackend.shared.application.result.Result;

public interface ElderlyProfileCommandService {
    Result<ElderlyProfile, CareCommandFailure> handle(CreateElderlyProfileCommand command);
}