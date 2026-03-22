package com.acnecare.api.treatment_case.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acnecare.api.treatment_case.entity.TreatmentCase;

@Repository
public interface TreatmentCaseRepository extends JpaRepository<TreatmentCase, String> {

    Optional<TreatmentCase> findByPatientIdAndDoctorIdAndStatus(
            String patientId, String doctorId, String status);

    List<TreatmentCase> findByPatientIdOrderByCreatedAtDesc(String patientId);
}
