package com.acnecare.api.product.controller;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.product.dto.request.ProductCreationRequest;
import com.acnecare.api.product.dto.request.ProductUpdateRequest;
import com.acnecare.api.product.dto.response.ProductResponse;
import com.acnecare.api.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    // Lúc nãy ở Service đã cấp quyền cho 3 Role, nên không cần gán lại ở Controller
    // nếu không muốn,
    // nhưng gán ở cả Service và Controller là an toàn nhất.
    @PostMapping(consumes = { "multipart/form-data" })
    ApiResponse<ProductResponse> createProduct(@Valid @ModelAttribute ProductCreationRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .message("Product created successfully")
                .result(productService.createProduct(request))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    ApiResponse<ProductResponse> updateProduct(
            @PathVariable String id,
            @Valid @ModelAttribute ProductUpdateRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .message("Product updated successfully")
                .result(productService.updateProduct(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Product deleted successfully")
                .build();
    }

    @GetMapping
    ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .code(1000)
                .message("Products retrieved successfully")
                .result(productService.getAllProducts())
                .build();
    }

    @PatchMapping("/{id}/approval-status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<ProductResponse> updateApprovalStatus(
            @PathVariable String id,
            @RequestParam String status) {
        return ApiResponse.<ProductResponse>builder()
                .code(1000)
                .message("Cập nhật trạng thái thành công")
                .result(productService.updateApprovalStatus(id, status))
                .build();
    }

}