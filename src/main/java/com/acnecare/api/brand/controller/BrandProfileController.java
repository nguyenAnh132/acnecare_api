package com.acnecare.api.brand.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.brand.dto.request.BrandProfileUpdateRequest;
import com.acnecare.api.brand.dto.response.BrandProfileResponse;
import com.acnecare.api.brand.service.BrandProfileService;
import com.acnecare.api.common.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/brands")
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
    
    @PutMapping
    ApiResponse<BrandProfileResponse> updateBrandProfile(@RequestBody @Valid BrandProfileUpdateRequest request) {
        return ApiResponse.<BrandProfileResponse>builder()
            .code(1000)
            .message("Brand Profile has been updated successfully")
            .result(brandProfileService.updateMyBrandProfile(request))
            .build();   
}

    @GetMapping("/{id}")
    ApiResponse<BrandProfileResponse> getBrandProfileById(String id){
        return ApiResponse.<BrandProfileResponse>builder()
            .code(1000)
            .message("Brand Profile has been retrieved successfully")
            .result(brandProfileService.getBrandProfileById(id))
            .build();
    }

}
