package com.famicare.FamicareBackend.care.domain.model.aggregates;

import com.famicare.FamicareBackend.care.domain.model.commands.InviteCaregiverCommand;
import com.famicare.FamicareBackend.care.domain.model.valueobjects.CaregiverRole;
import com.famicare.FamicareBackend.care.domain.model.valueobjects.EmailAddress;
import com.famicare.FamicareBackend.care.domain.model.valueobjects.InvitationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

@Entity
@Table(name = "caregivers", indexes = {
        @Index(name = "idx_caregivers_elderly_profile_id", columnList = "elderly_profile_id")
})
@Getter
@NoArgsConstructor
public class Caregiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long elderlyProfileId;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, length = 5)
    private String initials;

    @Embedded
    private EmailAddress email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CaregiverRole role;

    @Column(nullable = false, length = 120)
    private String relationshipLabel;

    @Column(nullable = false, length = 60)
    private String availability;

    @Column(nullable = false, length = 100)
    private String schedule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvitationStatus status;

    @Column(nullable = false, updatable = false)
    private Instant invitedAt;

    private Instant acceptedAt;

    public Caregiver(InviteCaregiverCommand command) {
        this.elderlyProfileId = command.elderlyProfileId();
        this.fullName = command.fullName().trim();
        this.initials = computeInitials(this.fullName);
        this.email = new EmailAddress(command.email());
        this.role = command.role() != null ? command.role() : CaregiverRole.COLLABORATOR;
        this.relationshipLabel = (command.relationshipLabel() != null && !command.relationshipLabel().isBlank())
                ? command.relationshipLabel().trim()
                : (this.role == CaregiverRole.ADMINISTRATOR
                ? "Cuidador - Administrador"
                : "Familiar - Colaborador");
        this.schedule = (command.schedule() != null && !command.schedule().isBlank())
                ? command.schedule().trim()
                : "Todos los dias";
        this.invitedAt = Instant.now();

        if (this.role == CaregiverRole.ADMINISTRATOR) {
            this.status = InvitationStatus.ACTIVE;
            this.availability = "En linea";
            this.acceptedAt = this.invitedAt;
        } else {
            this.status = InvitationStatus.PENDING;
            this.availability = "Pendiente";
        }
    }

    public boolean acceptInvitation() {
        if (this.status != InvitationStatus.PENDING) {
            return false;
        }
        this.status = InvitationStatus.ACTIVE;
        this.availability = "Disponible";
        this.acceptedAt = Instant.now();
        return true;
    }

    public boolean revokeAccess() {
        if (this.status == InvitationStatus.REVOKED) {
            return false;
        }
        this.status = InvitationStatus.REVOKED;
        this.availability = "Acceso eliminado";
        return true;
    }

    private static String computeInitials(String name) {
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 0) return "FC";
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        }
        return Arrays.stream(parts)
                .limit(2)
                .map(p -> p.substring(0, 1).toUpperCase())
                .collect(Collectors.joining());
    }
}