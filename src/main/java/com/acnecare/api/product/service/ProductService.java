package com.acnecare.api.product.service;

import com.acnecare.api.category.entity.Category;
import com.acnecare.api.category.repository.CategoryRepository;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.product.dto.request.ProductCreationRequest;
import com.acnecare.api.product.dto.request.ProductUpdateRequest;
import com.acnecare.api.product.dto.response.ProductResponse;
import com.acnecare.api.product.entity.Product;
import com.acnecare.api.product.mapper.ProductMapper;
import com.acnecare.api.product.repository.ProductRepository;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    UserRepository userRepository;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_BRAND')")
    public ProductResponse createProduct(ProductCreationRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Product product = productMapper.toProduct(request);
        product.setCategory(category);
        product.setCreatedBy(user);
        product.setCreatedAt(LocalDateTime.now());

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            product.setApprovalStatus("APPROVED");
        } else {
            product.setApprovalStatus("PENDING");
        }

        return productMapper.toProductResponse(productRepository.save(product));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_BRAND')")
    public ProductResponse updateProduct(String id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!product.getName().equals(request.getName()) && productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        productMapper.updateProduct(request, product);
        product.setCategory(category);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
    }

    public List<ProductResponse> getAllProducts() {
        if (isInternalStaff()) {
            return productMapper.toProductResponseList(productRepository.findAll());
        }
        return productMapper.toProductResponseList(productRepository.findByApprovalStatus("APPROVED"));
    }

    public List<ProductResponse> getProductsByCategoryId(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        if (isInternalStaff()) {
            return productMapper.toProductResponseList(productRepository.findByCategoryId(categoryId));
        }
        return productMapper
                .toProductResponseList(productRepository.findByCategoryIdAndApprovalStatus(categoryId, "APPROVED"));
    }

    private boolean isInternalStaff() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") ||
                        a.getAuthority().equals("ROLE_DOCTOR") ||
                        a.getAuthority().equals("ROLE_BRAND"));
    }
}