package com.acnecare.api.product.entity;

import com.acnecare.api.category.entity.Category;
import com.acnecare.api.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    String id;

    @Column(name = "product_name")
    String name;

    String brand;

    @Column(columnDefinition = "TEXT")
    String description;

    String thumbnailUrl;

    @Column(columnDefinition = "TEXT")
    String imagesUrl;

    @Column(columnDefinition = "TEXT")
    String ingredients;

    String affiliateUrl;

    // Trạng thái duyệt: "APPROVED" hoặc "PENDING"
    String approvalStatus;

    LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    // Đã đổi tên field và column thành created_by
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    User createdBy;
}