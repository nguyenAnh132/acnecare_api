package com.acnecare.api.doctor.repository;

import org.springframework.stereotype.Repository;

import com.acnecare.api.doctor.entity.DoctorProfile;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, String> {
    
}
