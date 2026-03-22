package com.acnecare.api.patient_routine.repository;

import com.acnecare.api.patient_routine.entity.Bag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BagRepository extends JpaRepository<Bag, String> {
    Optional<Bag> findByUserId(String userId);
}