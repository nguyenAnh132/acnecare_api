package com.acnecare.api.appointment.repository;

import com.acnecare.api.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

        List<Appointment> findByPatientIdOrderByAppointmentTimeDesc(String patientId);

        List<Appointment> findByDoctorIdOrderByAppointmentTimeDesc(String doctorId);

        List<Appointment> findByDoctorIdAndStatusOrderByAppointmentTimeAsc(String doctorId, String status);

        boolean existsByDoctorIdAndAppointmentTimeAndStatusNotIn(
                        String doctorId,
                        java.time.LocalDateTime appointmentTime,
                        Collection<String> statuses);

        List<Appointment> findByDoctorIdAndAppointmentTimeBetween(String doctorId, java.time.LocalDateTime start,
                        java.time.LocalDateTime end);
}