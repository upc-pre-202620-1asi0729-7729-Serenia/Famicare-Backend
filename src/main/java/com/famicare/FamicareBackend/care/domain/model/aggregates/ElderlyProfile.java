package com.famicare.FamicareBackend.care.domain.model.aggregates;

import com.famicare.FamicareBackend.care.domain.model.commands.CreateElderlyProfileCommand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "elderly_profiles")
@Getter
@NoArgsConstructor
public class ElderlyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false)
    private Integer age;

    @Column(length = 500)
    private String medicalNotes;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public ElderlyProfile(CreateElderlyProfileCommand command) {
        this.fullName = command.fullName().trim();
        this.age = command.age();
        this.medicalNotes = command.medicalNotes() != null ? command.medicalNotes().trim() : null;
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
}