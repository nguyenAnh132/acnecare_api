package com.acnecare.api.brand.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.acnecare.api.brand.dto.request.BrandProfileUpdateRequest;
import com.acnecare.api.brand.dto.response.BrandProfileResponse;
import com.acnecare.api.brand.entity.BrandProfile;

@Mapper(componentModel = "spring")
public interface BrandProfileMapper {
        BrandProfile toBrandProfile(BrandProfileUpdateRequest request);

        BrandProfileResponse toBrandProfileResponse(BrandProfile brandProfile);

        void updateBrand(BrandProfileUpdateRequest request, @MappingTarget BrandProfile brandProfile);
}
