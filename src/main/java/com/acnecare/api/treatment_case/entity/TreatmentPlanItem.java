package com.acnecare.api.treatment_case.entity;

import com.acnecare.api.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Table(name = "treatment_plan_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentPlanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id")
    String id;

    @Column(name = "custom_name")
    String customName; // Dùng khi bác sĩ kê thuốc ngoài hệ thống (không có product_id)

    @Column(name = "step_order")
    Integer stepOrder;

    @Column(name = "time_slot", length = 20)
    String timeSlot; // MORNING, AFTERNOON, EVENING

    @Column(name = "usage_instruction", columnDefinition = "TEXT")
    String usageInstruction;

    @Column(name = "duration_days")
    Integer durationDays;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    TreatmentPlan treatmentPlan;

    // Có thể null nếu dùng custom_name
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

    @PrePersist
    public void prePersist() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
    }
}