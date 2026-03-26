package com.acnecare.api.doctorschedule.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.common.helper.CurrentUserId;
import com.acnecare.api.consultation_service.repository.ConsultationServiceRepository;
import com.acnecare.api.doctor.repository.DoctorProfileRepository;
import com.acnecare.api.doctorschedule.dto.request.DoctorScheduleCreationRequest;
import com.acnecare.api.doctorschedule.dto.request.DoctorScheduleUpdateRequest;
import com.acnecare.api.doctorschedule.dto.response.DoctorScheduleResponse;
import com.acnecare.api.doctorschedule.entity.DoctorSchedule;
import com.acnecare.api.doctorschedule.mapper.DoctorScheduleMapper;
import com.acnecare.api.doctorschedule.repository.DoctorScheduleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DoctorScheduleService {

        DoctorScheduleRepository doctorScheduleRepository;
        DoctorScheduleMapper doctorScheduleMapper;
        DoctorProfileRepository doctorProfileRepository;
        ConsultationServiceRepository consultationServiceRepository;

        @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
        public DoctorScheduleResponse createMySchedule(DoctorScheduleCreationRequest request) {
                var userId = CurrentUserId.getCurrentUserId()
                                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

                var doctorProfile = doctorProfileRepository.findById(userId)
                                .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_PROFILE_NOT_FOUND));

                validateScheduleTime(request.getStartTime(), request.getEndTime());

                boolean overlapped = doctorScheduleRepository.existsByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThan(
                                userId,
                                request.getEndTime(),
                                request.getStartTime());

                if (overlapped) {
                        throw new AppException(ErrorCode.DOCTOR_SCHEDULE_TIME_CONFLICT);
                }

                DoctorSchedule doctorSchedule = doctorScheduleMapper.toDoctorSchedule(request);
                doctorSchedule.setDoctor(doctorProfile);

                if (request.getConsultationServiceId() != null && !request.getConsultationServiceId().isBlank()) {
                        var consultationService = consultationServiceRepository
                                        .findByIdAndDoctorId(request.getConsultationServiceId(), userId)
                                        .orElseThrow(() -> new AppException(ErrorCode.CONSULTATION_SERVICE_NOT_FOUND));

                        doctorSchedule.setConsultationService(consultationService);
                }

                return doctorScheduleMapper.toDoctorScheduleResponse(
                                doctorScheduleRepository.save(doctorSchedule));
        }

        @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
        public List<DoctorScheduleResponse> getMySchedules() {
                var userId = CurrentUserId.getCurrentUserId()
                                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

                doctorProfileRepository.findById(userId)
                                .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_PROFILE_NOT_FOUND));

                return doctorScheduleRepository.findByDoctorIdOrderByStartTimeAsc(userId).stream()
                                .map(doctorScheduleMapper::toDoctorScheduleResponse)
                                .toList();
        }

        @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
        public DoctorScheduleResponse updateMySchedule(String scheduleId, DoctorScheduleUpdateRequest request) {
                var userId = CurrentUserId.getCurrentUserId()
                                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

                var doctorSchedule = doctorScheduleRepository.findByIdAndDoctorId(scheduleId, userId)
                                .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_SCHEDULE_NOT_FOUND));

                validateScheduleTime(request.getStartTime(), request.getEndTime());

                boolean overlapped = doctorScheduleRepository.findByDoctorIdOrderByStartTimeAsc(userId).stream()
                                .anyMatch(item -> !item.getId().equals(scheduleId)
                                                && request.getStartTime().isBefore(item.getEndTime())
                                                && request.getEndTime().isAfter(item.getStartTime()));

                if (overlapped) {
                        throw new AppException(ErrorCode.DOCTOR_SCHEDULE_TIME_CONFLICT);
                }

                doctorScheduleMapper.updateDoctorSchedule(request, doctorSchedule);

                if (request.getConsultationServiceId() != null && !request.getConsultationServiceId().isBlank()) {
                        var consultationService = consultationServiceRepository
                                        .findByIdAndDoctorId(request.getConsultationServiceId(), userId)
                                        .orElseThrow(() -> new AppException(ErrorCode.CONSULTATION_SERVICE_NOT_FOUND));

                        doctorSchedule.setConsultationService(consultationService);
                } else {
                        doctorSchedule.setConsultationService(null);
                }

                return doctorScheduleMapper.toDoctorScheduleResponse(
                                doctorScheduleRepository.save(doctorSchedule));
        }

        @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
        public void deleteMySchedule(String scheduleId) {
                var userId = CurrentUserId.getCurrentUserId()
                                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

                var doctorSchedule = doctorScheduleRepository.findByIdAndDoctorId(scheduleId, userId)
                                .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_SCHEDULE_NOT_FOUND));

                doctorScheduleRepository.delete(doctorSchedule);
        }

        // Thêm tham số String serviceId vào hàm
        public List<DoctorScheduleResponse> getSchedulesByDoctorId(String doctorId, String serviceId, LocalDate date) {
                doctorProfileRepository.findById(doctorId)
                                .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_PROFILE_NOT_FOUND));

                LocalDateTime start = date.atStartOfDay();
                LocalDateTime end = date.plusDays(1).atStartOfDay();

                // Kiểm tra nếu không truyền serviceId thì trả về mảng rỗng (Bắt buộc phải chọn
                // dịch vụ)
                if (serviceId == null || serviceId.isBlank()) {
                        return List.of();
                }

                // Dùng hàm Repository mới tạo ở trên
                return doctorScheduleRepository
                                .findByDoctorIdAndConsultationServiceIdAndStartTimeLessThanAndEndTimeGreaterThanOrderByStartTimeAsc(
                                                doctorId, serviceId, end, start)
                                .stream()
                                .filter(item -> "AVAILABLE".equals(item.getStatus()))
                                .map(doctorScheduleMapper::toDoctorScheduleResponse)
                                .toList();
        }

        public DoctorScheduleResponse getScheduleById(String scheduleId) {
                var doctorSchedule = doctorScheduleRepository.findById(scheduleId)
                                .orElseThrow(() -> new AppException(ErrorCode.DOCTOR_SCHEDULE_NOT_FOUND));

                return doctorScheduleMapper.toDoctorScheduleResponse(doctorSchedule);
        }

        private void validateScheduleTime(LocalDateTime startTime, LocalDateTime endTime) {
                if (!endTime.isAfter(startTime)) {
                        throw new AppException(ErrorCode.INVALID_DOCTOR_SCHEDULE_TIME);
                }
        }
}
