package com.acnecare.api.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import java.util.Date;
import jakarta.persistence.Id;
import jakarta.persistence.Index;

@Entity
@Table(name = "invalidated_tokens", 
    indexes = @Index(
        name = "idx_expiry_time", 
        columnList = "expiry_time ASC", 
        unique = false))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidatedToken {
    @Id
    String id;
    Date expiryTime;
}
