package com.famicare.FamicareBackend.care.interfaces.rest.transform;

import com.famicare.FamicareBackend.care.domain.model.commands.InviteCaregiverCommand;
import com.famicare.FamicareBackend.care.domain.model.valueobjects.CaregiverRole;
import com.famicare.FamicareBackend.care.interfaces.rest.resources.InviteCaregiverResource;

public class InviteCaregiverCommandFromResourceAssembler {
    public static InviteCaregiverCommand toCommandFromResource(InviteCaregiverResource resource) {
        CaregiverRole parsedRole = CaregiverRole.COLLABORATOR;
        if (resource.role() != null && !resource.role().isBlank()) {
            parsedRole = CaregiverRole.valueOf(resource.role().trim().toUpperCase());
        }
        return new InviteCaregiverCommand(
                resource.elderlyProfileId(),
                resource.fullName(),
                resource.email(),
                parsedRole,
                resource.relationshipLabel(),
                resource.schedule()
        );
    }
}