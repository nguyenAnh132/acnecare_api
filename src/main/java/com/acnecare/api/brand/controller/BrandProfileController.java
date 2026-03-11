package com.acnecare.api.brand.controller;

import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.brand.dto.request.BrandProfileCreationRequest;
import com.acnecare.api.brand.dto.response.BrandProfileResponse;
import com.acnecare.api.brand.service.BrandProfileService;
import com.acnecare.api.common.dto.ApiResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/brand-profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class BrandProfileController {
    BrandProfileService brandProfileService;

    @GetMapping
    ApiResponse<BrandProfileResponse> getMyBrandProfile() {
        ApiResponse<BrandProfileResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(brandProfileService.getMyBrandProfile());

        return apiResponse;
    }
    
    @PostMapping
    ApiResponse<BrandProfileResponse> createMyBrandProfile(@RequestBody BrandProfileCreationRequest request) {
        return ApiResponse.<BrandProfileResponse>builder()
            .code(1000)
            .message("Brand Profile has been created successfully")
            .result(brandProfileService.createBrandProfileResponse(request))
            .build();
    } 
    
    @PutMapping
    ApiResponse<BrandProfileResponse> updateBrandProfile(@RequestBody BrandProfileCreationRequest request) {
        return ApiResponse.<BrandProfileResponse>builder()
            .code(1000)
            .message("Brand Profile has been updated successfully")
            .result(brandProfileService.updateBrandProfile(request))
            .build();   
}
}
