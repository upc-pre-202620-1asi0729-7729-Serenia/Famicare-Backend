package com.famicare.FamicareBackend.care.domain.model.commands;

import com.famicare.FamicareBackend.care.domain.model.valueobjects.CaregiverRole;

public record InviteCaregiverCommand(
        Long elderlyProfileId,
        String fullName,
        String email,
        CaregiverRole role,
        String relationshipLabel,
        String schedule
) {}