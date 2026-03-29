package com.acnecare.api.treatment_case.service;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.patient_routine.entity.DailyRoutine;
import com.acnecare.api.patient_routine.entity.DailyRoutineStep;
import com.acnecare.api.patient_routine.enums.TimeOfDay;
import com.acnecare.api.patient_routine.repository.DailyRoutineRepository;
import com.acnecare.api.patient_routine.repository.DailyRoutineStepRepository;
import com.acnecare.api.product.entity.Product;
import com.acnecare.api.product.repository.ProductRepository;
import com.acnecare.api.treatment_case.dto.request.TreatmentPlanCreationRequest;
import com.acnecare.api.treatment_case.entity.TreatmentCase;
import com.acnecare.api.treatment_case.entity.TreatmentPlan;
import com.acnecare.api.treatment_case.entity.TreatmentPlanItem;
import com.acnecare.api.treatment_case.repository.TreatmentCaseRepository;
import com.acnecare.api.treatment_case.repository.TreatmentPlanRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j // THÊM DÒNG NÀY ĐỂ BẬT TÍNH NĂNG LOG
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentPlanService {

    TreatmentPlanRepository planRepository;
    TreatmentCaseRepository caseRepository;
    ProductRepository productRepository;

    DailyRoutineRepository dailyRoutineRepository;
    DailyRoutineStepRepository dailyRoutineStepRepository;

    // 1. BÁC SĨ TẠO PHÁC ĐỒ ĐIỀU TRỊ
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public String createPlan(String caseId, TreatmentPlanCreationRequest request) {
        try {
            String currentDoctorId = SecurityContextHolder.getContext().getAuthentication().getName();

            TreatmentCase treatmentCase = caseRepository.findById(caseId)
                    .orElseThrow(() -> new AppException(ErrorCode.TREATMENT_CASE_NOT_FOUND));

            if (!treatmentCase.getDoctor().getId().equals(currentDoctorId)) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }

            TreatmentPlan plan = new TreatmentPlan();
            plan.setNotes(request.getNotes());
            plan.setTreatmentCase(treatmentCase);

            // Bắt lỗi ngay tại đây nếu frontend không gửi mảng items lên (null)
            if (request.getItems() == null) {
                log.error("Dữ liệu items từ Frontend gửi lên đang bị NULL!");
                throw new IllegalArgumentException("Danh sách sản phẩm (items) không được để trống!");
            }

            List<TreatmentPlanItem> items = request.getItems().stream().map(req -> {
                TreatmentPlanItem item = new TreatmentPlanItem();
                item.setStepOrder(req.getStepOrder());
                item.setTimeSlot(req.getTimeSlot());
                item.setUsageInstruction(req.getUsageInstruction());
                item.setDurationDays(req.getDurationDays());
                item.setTreatmentPlan(plan);

                if (req.getProductId() != null && !req.getProductId().trim().isEmpty()) {
                    Product product = productRepository.findById(req.getProductId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                    item.setProduct(product);
                    item.setCustomName(null);
                } else {
                    item.setProduct(null);
                    item.setCustomName(req.getCustomName());
                }

                return item;
            }).collect(Collectors.toList());

            plan.setItems(items);
            planRepository.save(plan);
            return "Tạo phác đồ điều trị thành công!";

        } catch (Exception e) {
            // IN RA TOÀN BỘ CHI TIẾT LỖI Ở TERMINAL ĐỂ BẮT BỆNH
            log.error("==== LỖI KHI LƯU PHÁC ĐỒ ====", e);
            e.printStackTrace();
            throw e; // Bắt buộc ném lại lỗi để @Transactional rollback database
        }
    }

    // 2. BỆNH NHÂN MAP PHÁC ĐỒ VÀO ROUTINE HÀNG NGÀY
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_PATIENT')")
    public String applyPlanToMyRoutine(String planId) {
        try {
            String currentPatientId = SecurityContextHolder.getContext().getAuthentication().getName();

            TreatmentPlan plan = planRepository.findById(planId)
                    .orElseThrow(() -> new AppException(ErrorCode.TREATMENT_PLAN_NOT_FOUND));

            if (!plan.getTreatmentCase().getPatient().getId().equals(currentPatientId)) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }

            DailyRoutine routine = new DailyRoutine();
            routine.setUser(plan.getTreatmentCase().getPatient());
            routine.setRoutineName(
                    "Phác đồ Bác sĩ " + plan.getTreatmentCase().getDoctor().getLastName() + " - " + LocalDate.now());
            routine.setNote(plan.getNotes());
            routine.setIsActive(true);

            routine = dailyRoutineRepository.save(routine);

            List<DailyRoutineStep> routineSteps = new ArrayList<>();
            int stepOrderCounter = 1;

            for (TreatmentPlanItem planItem : plan.getItems()) {
                if (planItem.getProduct() != null) {
                    DailyRoutineStep step = new DailyRoutineStep();
                    step.setDailyRoutine(routine);
                    step.setProduct(planItem.getProduct());

                    try {
                        step.setTimeOfDay(TimeOfDay.valueOf(planItem.getTimeSlot().toUpperCase()));
                    } catch (Exception e) {
                        step.setTimeOfDay(TimeOfDay.MORNING);
                    }

                    step.setStepOrder(stepOrderCounter++);
                    step.setNotes(planItem.getUsageInstruction());

                    routineSteps.add(step);
                }
            }

            if (routineSteps.isEmpty()) {
                throw new AppException(ErrorCode.ERROR_INVALID_REQUEST);
            }

            dailyRoutineStepRepository.saveAll(routineSteps);

            return "Đã đồng bộ phác đồ của Bác sĩ vào Lịch trình chăm sóc da của bạn!";

        } catch (Exception e) {
            // IN LỖI
            log.error("==== LỖI KHI ĐỒNG BỘ ROUTINE ====", e);
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public String updatePlan(String planId, TreatmentPlanCreationRequest request) {
        try {
            String currentDoctorId = SecurityContextHolder.getContext().getAuthentication().getName();
            
            TreatmentPlan plan = planRepository.findById(planId)
                    .orElseThrow(() -> new AppException(ErrorCode.TREATMENT_PLAN_NOT_FOUND));

            if (!plan.getTreatmentCase().getDoctor().getId().equals(currentDoctorId)) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }

            // Cập nhật Ghi chú
            plan.setNotes(request.getNotes());
            
            // Xóa hết các Item cũ để nạp lại Item mới (Tránh bị trùng lặp)
            plan.getItems().clear();

            if (request.getItems() != null) {
                List<TreatmentPlanItem> newItems = request.getItems().stream().map(req -> {
                    TreatmentPlanItem item = new TreatmentPlanItem();
                    item.setStepOrder(req.getStepOrder());
                    item.setTimeSlot(req.getTimeSlot());
                    item.setUsageInstruction(req.getUsageInstruction());
                    item.setDurationDays(req.getDurationDays());
                    item.setTreatmentPlan(plan);

                    if (req.getProductId() != null && !req.getProductId().trim().isEmpty()) {
                        Product product = productRepository.findById(req.getProductId())
                                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                        item.setProduct(product);
                    } else {
                        item.setCustomName(req.getCustomName());
                    }
                    return item;
                }).collect(Collectors.toList());

                plan.getItems().addAll(newItems);
            }

            planRepository.save(plan);
            return "Cập nhật phác đồ điều trị thành công!";
        } catch (Exception e) {
            log.error("==== LỖI KHI CẬP NHẬT PHÁC ĐỒ ====", e);
            throw e;
        }
    }
}