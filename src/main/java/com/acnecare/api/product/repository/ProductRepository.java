package com.acnecare.api.product.repository;

import com.acnecare.api.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByCategoryId(String categoryId);

    // Lấy toàn bộ sản phẩm theo một trạng thái (Dành cho Patient/Guest)
    List<Product> findByApprovalStatus(String approvalStatus);

    // Lấy sản phẩm trong 1 danh mục theo trạng thái (Dành cho Patient/Guest)
    List<Product> findByCategoryIdAndApprovalStatus(String categoryId, String approvalStatus);

    boolean existsByCategoryId(String categoryId);

    boolean existsByName(String name);
}