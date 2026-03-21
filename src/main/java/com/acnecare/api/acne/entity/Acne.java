package com.acnecare.api.acne.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFilter;

@Entity
@Table(name = "acnes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonFilter("snakeCaseFilter")
public class Acne {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "code_name", unique = true, nullable = false)
    String codeName; // Dùng để AI map dữ liệu (vd: "dark spot", "acne")

    @Column(name = "name", nullable = false)
    String name; // Tên tiếng Việt (vd: "Vết thâm")

    @Column(name = "description")
    String description;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;
}