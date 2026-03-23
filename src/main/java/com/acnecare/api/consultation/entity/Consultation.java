package com.acnecare.api.consultation.entity;


import com.acnecare.api.appointment.entity.Appointment;
import com.acnecare.api.treatment_case.entity.TreatmentCase;
import com.acnecare.api.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "consultation_id")
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    TreatmentCase treatmentCase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    User doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    Appointment appointment;

    @Column(name = "consultation_at", nullable = false)
    LocalDateTime consultationAt;

    @Column(name = "assessment", columnDefinition = "TEXT")
    String assessment;   // đánh giá tình trạng da

    @Column(name = "plan_summary", columnDefinition = "TEXT")
    String planSummary;  // kế hoạch điều trị

    @Column(name = "doctor_notes", columnDefinition = "TEXT")
    String doctorNotes;  // ghi chú thêm của bác sĩ

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (consultationAt == null) consultationAt = LocalDateTime.now();
    }
}
