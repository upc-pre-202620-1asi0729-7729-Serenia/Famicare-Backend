package com.famicare.FamicareBackend.care.domain.model.commands;

public record CreateElderlyProfileCommand(
        String fullName,
        Integer age,
        String medicalNotes
) {}