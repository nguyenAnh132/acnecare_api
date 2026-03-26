package com.acnecare.api.patient_routine.repository;

import com.acnecare.api.patient_routine.entity.BagItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BagItemRepository extends JpaRepository<BagItem, String> {
    boolean existsByBagIdAndProductId(String bagId, String productId);
}