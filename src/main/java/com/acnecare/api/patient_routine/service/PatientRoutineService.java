package com.acnecare.api.patient_routine.service;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.patient_routine.dto.request.RoutineCreationRequest;
import com.acnecare.api.patient_routine.dto.request.BagItemCreationRequest;
import com.acnecare.api.patient_routine.dto.response.RoutineResponse;
import com.acnecare.api.patient_routine.entity.*;
import com.acnecare.api.patient_routine.mapper.PatientRoutineMapper;
import com.acnecare.api.patient_routine.repository.*;
import com.acnecare.api.product.entity.Product;
import com.acnecare.api.product.repository.ProductRepository;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientRoutineService {

    DailyRoutineRepository routineRepository;
    DailyRoutineStepRepository stepRepository;
    BagRepository bagRepository;
    BagItemRepository bagItemRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    PatientRoutineMapper routineMapper;

    // A. LOGIC CHO BAG (TÚI ĐỒ SKINCARE)

    private Bag getOrCreateBag(User user) {
        return bagRepository.findByUserId(user.getId())
                .orElseGet(() -> bagRepository.save(Bag.builder().user(user).build()));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public void addProductToBag(BagItemCreationRequest request) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Bag bag = getOrCreateBag(user);

        if (bagItemRepository.existsByBagIdAndProductId(bag.getId(), product.getId())) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS); // Đã có trong túi
        }

        bagItemRepository.save(BagItem.builder()
                .bag(bag)
                .product(product)
                .build());
    }

    // B. LOGIC CHO DAILY ROUTINE

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public RoutineResponse createMyRoutine(RoutineCreationRequest request) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(currentUserId).orElseThrow();

        DailyRoutine routine = routineMapper.toDailyRoutine(request);
        routine.setUser(user);

        List<DailyRoutineStep> steps = new ArrayList<>();
        if (request.getSteps() != null) {
            AtomicInteger order = new AtomicInteger(1);
            for (var stepReq : request.getSteps()) {
                Product product = productRepository.findById(stepReq.getProductId()).orElseThrow();
                steps.add(DailyRoutineStep.builder()
                        .dailyRoutine(routine)
                        .product(product)
                        .timeOfDay(stepReq.getTimeOfDay())
                        .stepOrder(stepReq.getStepOrder() != null ? stepReq.getStepOrder() : order.getAndIncrement())
                        .notes(stepReq.getNotes())
                        .build());
            }
        }
        routine.setSteps(steps);

        return routineMapper.toRoutineResponse(routineRepository.save(routine));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public RoutineResponse updateMyRoutine(String routineId, RoutineCreationRequest request) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        DailyRoutine routine = routineRepository.findById(routineId).orElseThrow();
        if (!routine.getUser().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        routineMapper.updateDailyRoutine(request, routine);
        routine.setUpdatedAt(LocalDateTime.now());

        stepRepository.deleteByDailyRoutine(routine); // Xóa bước cũ

        List<DailyRoutineStep> steps = new ArrayList<>();
        if (request.getSteps() != null) {
            AtomicInteger order = new AtomicInteger(1);
            for (var stepReq : request.getSteps()) {
                Product product = productRepository.findById(stepReq.getProductId()).orElseThrow();
                steps.add(DailyRoutineStep.builder()
                        .dailyRoutine(routine)
                        .product(product)
                        .timeOfDay(stepReq.getTimeOfDay())
                        .stepOrder(stepReq.getStepOrder() != null ? stepReq.getStepOrder() : order.getAndIncrement())
                        .notes(stepReq.getNotes())
                        .build());
            }
        }
        routine.setSteps(steps);

        return routineMapper.toRoutineResponse(routineRepository.save(routine));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public void deleteMyRoutine(String routineId) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        DailyRoutine routine = routineRepository.findById(routineId).orElseThrow();

        if (!routine.getUser().getId().equals(currentUserId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        routineRepository.delete(routine);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public List<RoutineResponse> getMyRoutines() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        return routineMapper.toRoutineResponses(routineRepository.findByUserId(currentUserId));
    }
}