package com.acnecare.api.consultation.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationCreationRequest {

    String appointmentId;   // bắt buộc — lấy từ lịch khám
    String assessment;      // đánh giá tình trạng da
    String planSummary;     // kế hoạch điều trị
    String doctorNotes;     // ghi chú thêm
    String chiefComplaint;  // lý do khám (chỉ dùng khi tạo TreatmentCase mới)
}
