package com.acnecare.api.acne.mapper;

import org.mapstruct.Mapper;

import com.acnecare.api.acne.dto.response.AcneResponse;
import com.acnecare.api.acne.entity.Acne;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AcneMapper {

    AcneResponse toAcneResponse(Acne acne);

    List<AcneResponse> toAcneResponseList(List<Acne> acnes);

}
