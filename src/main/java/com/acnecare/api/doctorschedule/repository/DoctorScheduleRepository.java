package com.acnecare.api.doctorschedule.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acnecare.api.doctorschedule.entity.DoctorSchedule;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, String> {

        List<DoctorSchedule> findByDoctorIdOrderByStartTimeAsc(String doctorId);

        List<DoctorSchedule> findByDoctorIdAndStatusOrderByStartTimeAsc(String doctorId, String status);

        List<DoctorSchedule> findByDoctorIdAndStartTimeBetweenOrderByStartTimeAsc(
                        String doctorId,
                        LocalDateTime start,
                        LocalDateTime end);

        Optional<DoctorSchedule> findByIdAndDoctorId(String id, String doctorId);

        boolean existsByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThan(
                        String doctorId,
                        LocalDateTime endTime,
                        LocalDateTime startTime);

        List<DoctorSchedule> findByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThanOrderByStartTimeAsc(
                        String doctorId,
                        java.time.LocalDateTime endOfDay,
                        java.time.LocalDateTime startOfDay);

        List<DoctorSchedule> findByDoctorIdAndConsultationServiceIdAndStartTimeLessThanAndEndTimeGreaterThanOrderByStartTimeAsc(
                        String doctorId,
                        String consultationServiceId,
                        java.time.LocalDateTime endOfDay,
                        java.time.LocalDateTime startOfDay);
}