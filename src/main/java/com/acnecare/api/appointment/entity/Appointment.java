package com.acnecare.api.appointment.entity;

import com.acnecare.api.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "acne_appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "appointment_id")
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    User doctor;

    @Column(name = "appointment_time", columnDefinition = "TIMESTAMP")
    LocalDateTime appointmentTime;

    @Column(name = "status", columnDefinition = "TEXT")
    String status;

    @Column(name = "mode", columnDefinition = "TEXT")
    String mode;

    @Column(name = "meeting_url", columnDefinition = "TEXT")
    String meetingUrl;

    @Column(name = "payment_method", columnDefinition = "TEXT")
    String paymentMethod;

    @Column(name = "payment_status", columnDefinition = "TEXT")
    String paymentStatus;

    @Column(name = "note", columnDefinition = "TEXT")
    String note;
    @Column(name = "review", columnDefinition = "TEXT")
    String review;

    @Column(name = "rating", columnDefinition = "INT")
    Integer rating;

    @Column(name = "service_id", columnDefinition = "VARCHAR(255)")
    String serviceId;

    @Column(name = "service_name", columnDefinition = "VARCHAR(255)")
    String serviceName;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    LocalDateTime createdAt;
}