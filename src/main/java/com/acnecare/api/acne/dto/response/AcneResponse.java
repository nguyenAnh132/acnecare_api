package com.acnecare.api.acne.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFilter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonFilter("snakeCaseFilter")
public class AcneResponse {
    String id;
    String name;
    String description;
    LocalDateTime createdAt;
}
