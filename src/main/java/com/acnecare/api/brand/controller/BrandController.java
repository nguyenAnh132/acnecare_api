package com.acnecare.api.brand.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acnecare.api.brand.dto.request.AdminUpdateProfileBrandRequest;
import com.acnecare.api.brand.dto.request.BrandProfileUpdateRequest;
import com.acnecare.api.brand.dto.response.BrandProfileResponse;
import com.acnecare.api.brand.service.BrandService;
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

public class BrandController {
    BrandService brandService;

    @GetMapping("/profile/me")
    ApiResponse<BrandProfileResponse> getMyBrandProfile() {
        ApiResponse<BrandProfileResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(brandService.getMyBrandProfile());

        return apiResponse;
    }    
    
    @PutMapping("/profile/me")
    ApiResponse<BrandProfileResponse> updateBrandProfile(@RequestBody @Valid BrandProfileUpdateRequest request) {
        return ApiResponse.<BrandProfileResponse>builder()
            .code(1000)
            .message("Brand Profile has been updated successfully")
            .result(brandService.updateMyBrandProfile(request))
            .build();   
    }

    @GetMapping("/profile/{id}")
    ApiResponse<BrandProfileResponse> getBrandProfileById(@PathVariable String id){
        return ApiResponse.<BrandProfileResponse>builder()
            .code(1000)
            .message("Brand Profile has been retrieved successfully")
            .result(brandService.getBrandProfileById(id))
            .build();
    }

    @PutMapping("/profile/{id}")
    ApiResponse<BrandProfileResponse> updateBrandProfileByAdmin(@PathVariable String id, @RequestBody @Valid AdminUpdateProfileBrandRequest request){
        return ApiResponse.<BrandProfileResponse>builder()
            .code(1000)
            .message("Brand Profile has been updated successfully")
            .result(brandService.updateBrandProfileByAdmin(id, request))
            .build();
    }
}
