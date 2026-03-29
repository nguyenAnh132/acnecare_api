package com.acnecare.api.patient_routine.repository;

import com.acnecare.api.patient_routine.entity.DailyRoutine;
import com.acnecare.api.patient_routine.entity.DailyRoutineStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRoutineStepRepository extends JpaRepository<DailyRoutineStep, String> {
    void deleteByDailyRoutine(DailyRoutine dailyRoutine);
}