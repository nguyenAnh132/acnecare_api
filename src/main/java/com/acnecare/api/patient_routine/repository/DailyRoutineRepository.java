package com.acnecare.api.patient_routine.repository;

import com.acnecare.api.patient_routine.entity.DailyRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DailyRoutineRepository extends JpaRepository<DailyRoutine, String> {
    List<DailyRoutine> findByUserId(String userId);
}