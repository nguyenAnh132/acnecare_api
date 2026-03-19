package com.acnecare.api.consultation_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acnecare.api.consultation_service.entity.ConsultationService;

@Repository
public interface ConsultationServiceRepository extends JpaRepository<ConsultationService, String> {

    List<ConsultationService> findByDoctorId(String doctorId);

    List<ConsultationService> findByDoctorIdAndIsActiveTrue(String doctorId);

    Optional<ConsultationService> findByIdAndDoctorId(String id, String doctorId);
}