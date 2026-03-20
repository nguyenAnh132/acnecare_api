package com.acnecare.api.patient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.acnecare.api.patient.entity.AcnePrediction;
import com.acnecare.api.patient.entity.PatientProfile;

@Repository
public interface AcnePredictionRepository extends JpaRepository<AcnePrediction, String> {

    List<AcnePrediction> findAllByPatient(PatientProfile patient);

}
