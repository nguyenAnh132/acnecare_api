package com.acnecare.api.patient.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.acnecare.api.patient.entity.PatientProfile;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, String> {

    boolean existsByUserId(String userId);

}
