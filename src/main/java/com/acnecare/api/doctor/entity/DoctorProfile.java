
package com.acnecare.api.doctor.entity;

import com.acnecare.api.consultation_service.entity.ConsultationService;
import com.acnecare.api.doctorschedule.entity.DoctorSchedule;
import com.acnecare.api.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "doctor_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorProfile {

    @Id
    @Column(name = "user_id")
    String id;

    LocalDate dob;

    @Column(name = "license_url", length = 255)
    String licenseUrl;
    String specialty;
    String bio;
    String clinicName;
    Integer yearsExperience;
    String verificationStatus;
    String rejectionReason; // lý do từ chối
    Double ratingAvg;
    Integer ratingCount;
    String address;
    boolean isAcceptingAppointments;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "doctor")
    List<ConsultationService> consultationServices;

    @OneToMany(mappedBy = "doctor")
List<DoctorSchedule> schedules;

}
