package com.famicare.FamicareBackend.care.interfaces.rest.transform;

import com.famicare.FamicareBackend.care.domain.model.commands.CreateElderlyProfileCommand;
import com.famicare.FamicareBackend.care.interfaces.rest.resources.CreateElderlyProfileResource;

public class CreateElderlyProfileCommandFromResourceAssembler {
    public static CreateElderlyProfileCommand toCommandFromResource(CreateElderlyProfileResource resource) {
        return new CreateElderlyProfileCommand(
                resource.fullName(),
                resource.age(),
                resource.medicalNotes()
        );
    }
}