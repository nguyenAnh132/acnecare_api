package com.acnecare.api.message.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import com.acnecare.api.message.dto.request.MessageRequest;
import com.acnecare.api.message.dto.response.MessageResponse;
import com.acnecare.api.message.dto.response.PagedResponse;
import com.acnecare.api.message.entity.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageResponse toMessageResponse(Message message);

    List<MessageResponse> toMessageResponseList(List<Message> messages);

    Message toMessageEntity(MessageRequest request);

    default PagedResponse<MessageResponse> toPagedResponse(Page<Message> page) {
        return PagedResponse.<MessageResponse>builder()
                .content(toMessageResponseList(page.getContent()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}