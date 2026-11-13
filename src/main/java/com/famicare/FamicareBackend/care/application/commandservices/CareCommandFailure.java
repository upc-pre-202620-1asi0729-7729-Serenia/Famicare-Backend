package com.famicare.FamicareBackend.care.application.commandservices;

public sealed interface CareCommandFailure {
    String messageKey();

    record ElderlyProfileNotFound() implements CareCommandFailure {
        @Override
        public String messageKey() { return "care.error.elderlyProfile.notFound"; }
    }

    record CaregiverNotFound() implements CareCommandFailure {
        @Override
        public String messageKey() { return "care.error.caregiver.notFound"; }
    }

    record CaregiverAlreadyInvited() implements CareCommandFailure {
        @Override
        public String messageKey() { return "care.error.caregiver.alreadyInvited"; }
    }

    record InvalidInvitationState() implements CareCommandFailure {
        @Override
        public String messageKey() { return "care.error.caregiver.invalidState"; }
    }
}