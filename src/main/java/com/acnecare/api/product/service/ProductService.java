package com.acnecare.api.product.service;

import com.acnecare.api.category.entity.Category;
import com.acnecare.api.category.repository.CategoryRepository;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.common.storage.FileStorageService;
import com.acnecare.api.common.storage.StorageFolder;
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
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    FileStorageService fileStorageService;

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

        // 1. XỬ LÝ UPLOAD ẢNH THUMBNAIL
        if (request.getThumbnailFile() != null && !request.getThumbnailFile().isEmpty()) {
            var fileRes = fileStorageService.store(request.getThumbnailFile(), StorageFolder.products, user.getId());
            product.setThumbnailUrl(fileRes.getUrl());
        }

        // 2. XỬ LÝ UPLOAD NHIỀU ẢNH PHỤ (IMAGES)
        if (request.getImageFiles() != null && !request.getImageFiles().isEmpty()) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : request.getImageFiles()) {
                if (!file.isEmpty()) {
                    var fileRes = fileStorageService.store(file, StorageFolder.products, user.getId());
                    imageUrls.add(fileRes.getUrl());
                }
            }
            // Gộp danh sách URL bằng dấu phẩy
            product.setImagesUrl(String.join(",", imageUrls));
        }

        product.setCategory(category);
        product.setCreatedBy(user);
        product.setCreatedAt(LocalDateTime.now());

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        product.setApprovalStatus(isAdmin ? "APPROVED" : "PENDING");

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

        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 1. Lưu lại list ảnh cũ mà user KHÔNG xóa (được truyền từ React)
        String retainedImagesUrl = request.getImagesUrl();

        // Cập nhật thông tin chữ
        productMapper.updateProduct(request, product);

        // 2. CẬP NHẬT ẢNH THUMBNAIL
        if (request.getThumbnailFile() != null && !request.getThumbnailFile().isEmpty()) {
            var fileRes = fileStorageService.store(request.getThumbnailFile(), StorageFolder.products, currentUserId);
            product.setThumbnailUrl(fileRes.getUrl());
        }

        // 3. CẬP NHẬT ẢNH PHỤ (GỘP CŨ VÀ MỚI)
        List<String> finalImageUrls = new ArrayList<>();

        // Đưa các ảnh cũ vào mảng
        if (retainedImagesUrl != null && !retainedImagesUrl.isBlank()) {
            finalImageUrls.add(retainedImagesUrl);
        }

        // Upload và nối thêm các ảnh mới vào mảng
        if (request.getImageFiles() != null && !request.getImageFiles().isEmpty()) {
            for (org.springframework.web.multipart.MultipartFile file : request.getImageFiles()) {
                if (!file.isEmpty()) {
                    var fileRes = fileStorageService.store(file, StorageFolder.products, currentUserId);
                    finalImageUrls.add(fileRes.getUrl());
                }
            }
        }

        // Lưu lại danh sách tổng hợp
        product.setImagesUrl(String.join(",", finalImageUrls));
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ProductResponse updateApprovalStatus(String id, String status) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setApprovalStatus(status); // Cập nhật thành APPROVED hoặc PENDING
        return productMapper.toProductResponse(productRepository.save(product));
    }
}