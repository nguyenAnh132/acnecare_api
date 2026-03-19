package com.acnecare.api.brand.entity;
import java.time.LocalDateTime;

import com.acnecare.api.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "brand_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE) 

public class BrandProfile {

    @Id
    String id;
    String brandName;
    String description;
    String website;
    String logoUrl;
    String verificationStatus;
    String rejectionReason;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")    
    User user;
}
