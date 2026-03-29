package com.acnecare.api.treatment_case.repository;

import com.acnecare.api.treatment_case.entity.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, String> {
    List<TreatmentPlan> findByTreatmentCaseIdOrderByCreatedAtDesc(String caseId);
}