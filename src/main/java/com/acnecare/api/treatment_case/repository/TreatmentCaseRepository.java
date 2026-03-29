package com.acnecare.api.treatment_case.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.acnecare.api.treatment_case.entity.TreatmentCase;

@Repository
public interface TreatmentCaseRepository extends JpaRepository<TreatmentCase, String> {

    Optional<TreatmentCase> findByPatientIdAndDoctorIdAndStatus(
            String patientId, String doctorId, String status);

    List<TreatmentCase> findByPatientIdOrderByCreatedAtDesc(String patientId);

    // THÊM HÀM NÀY: Dùng JOIN FETCH để lấy luôn thông tin Bệnh nhân và Lịch sử khám
    // tránh lỗi Lazy Load
    @Query("SELECT DISTINCT t FROM TreatmentCase t " +
            "JOIN FETCH t.patient " +
            "JOIN FETCH t.doctor " +
            "LEFT JOIN FETCH t.consultations " +
            "WHERE t.doctor.id = :doctorId " +
            "ORDER BY t.updatedAt DESC")
    List<TreatmentCase> findByDoctorIdOrderByUpdatedAtDesc(@Param("doctorId") String doctorId);
}