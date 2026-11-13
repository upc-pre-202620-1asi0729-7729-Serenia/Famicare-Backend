package com.famicare.FamicareBackend.care.infrastructure.persistence.jpa;

import com.famicare.FamicareBackend.care.domain.model.aggregates.Caregiver;
import com.famicare.FamicareBackend.care.domain.model.valueobjects.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {
    List<Caregiver> findByElderlyProfileIdAndStatusNot(Long elderlyProfileId, InvitationStatus status);
    boolean existsByElderlyProfileIdAndEmailValueAndStatusNot(Long elderlyProfileId, String email, InvitationStatus status);
}