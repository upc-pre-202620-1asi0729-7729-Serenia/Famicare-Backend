package com.famicare.FamicareBackend.care.infrastructure.persistence.jpa;

import com.famicare.FamicareBackend.care.domain.model.aggregates.ElderlyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElderlyProfileRepository extends JpaRepository<ElderlyProfile, Long> {
}