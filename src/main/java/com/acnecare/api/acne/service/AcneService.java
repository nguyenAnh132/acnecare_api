package com.acnecare.api.acne.service;

import org.springframework.stereotype.Service;

import com.acnecare.api.acne.repository.AcneRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import com.acnecare.api.acne.dto.response.AcneResponse;
import com.acnecare.api.acne.entity.Acne;
import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.acne.mapper.AcneMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AcneService {
    AcneRepository acneRepository; 
    AcneMapper acneMapper;
    
    public AcneResponse getAcneById(String id) {
        Acne acne = acneRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.ACNE_NOT_FOUND));
        return acneMapper.toAcneResponse(acne);
    }

    public List<AcneResponse> getAllAcnes() {
        List<Acne> acnes = acneRepository.findAll();
        return acneMapper.toAcneResponseList(acnes);
    }
}
