package com.acnecare.api.role.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.Builder;
import java.util.Set;
import com.acnecare.api.permission.entity.Permission;
import jakarta.persistence.ManyToMany;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    String name;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @ManyToMany
    Set<Permission> permissions;
}
