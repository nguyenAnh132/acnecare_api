package com.acnecare.api.patient_routine.entity;

import com.acnecare.api.patient_routine.enums.TimeOfDay;
import com.acnecare.api.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "daily_routine_steps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyRoutineStep {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    DailyRoutine dailyRoutine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    TimeOfDay timeOfDay;

    Integer stepOrder;

    @Column(columnDefinition = "TEXT")
    String notes;
}