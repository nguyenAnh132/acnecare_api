package com.acnecare.api.acne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.acnecare.api.acne.entity.AcnePrediction;
import java.util.List;

@Repository
public interface AcnePredictionRepository extends JpaRepository<AcnePrediction, String> {
    List<AcnePrediction> findByPatientIdOrderByCreatedAtDesc(String patientId);
}