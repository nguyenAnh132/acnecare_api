package com.acnecare.api.patient.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import com.acnecare.api.user.entity.User;
import java.time.LocalDateTime;
import jakarta.persistence.MapsId;
@Entity
@Table(name = "patient_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientProfile {

    @Id
    String id;
    boolean gender;
    String skinType;
    String allergies;
    double height;
    double weight;
    String address;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    User user;
}
