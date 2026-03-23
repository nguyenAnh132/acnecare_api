package com.acnecare.api.consultation.repository;

import com.acnecare.api.consultation.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, String> {

    List<Consultation> findByTreatmentCaseIdOrderByConsultationAtDesc(String caseId);
    boolean existsByAppointmentId(String appointmentId);
}
