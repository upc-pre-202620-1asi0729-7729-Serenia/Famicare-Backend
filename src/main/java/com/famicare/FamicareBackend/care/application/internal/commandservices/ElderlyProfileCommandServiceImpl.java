package com.famicare.FamicareBackend.care.application.internal.commandservices;

import com.famicare.FamicareBackend.care.application.commandservices.CareCommandFailure;
import com.famicare.FamicareBackend.care.application.commandservices.ElderlyProfileCommandService;
import com.famicare.FamicareBackend.care.domain.model.aggregates.ElderlyProfile;
import com.famicare.FamicareBackend.care.domain.model.commands.CreateElderlyProfileCommand;
import com.famicare.FamicareBackend.care.infrastructure.persistence.jpa.ElderlyProfileRepository;
import com.famicare.FamicareBackend.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ElderlyProfileCommandServiceImpl implements ElderlyProfileCommandService {

    private final ElderlyProfileRepository elderlyProfileRepository;

    public ElderlyProfileCommandServiceImpl(ElderlyProfileRepository elderlyProfileRepository) {
        this.elderlyProfileRepository = elderlyProfileRepository;
    }

    @Override
    @Transactional
    public Result<ElderlyProfile, CareCommandFailure> handle(CreateElderlyProfileCommand command) {
        var profile = new ElderlyProfile(command);
        return Result.logrado(elderlyProfileRepository.save(profile));
    }
}