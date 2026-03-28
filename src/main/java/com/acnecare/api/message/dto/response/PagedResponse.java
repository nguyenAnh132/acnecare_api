package com.acnecare.api.message.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagedResponse<T> {         

    List<T> content;
    int page;           
    int size;
    long totalElements;
    int totalPages;

}