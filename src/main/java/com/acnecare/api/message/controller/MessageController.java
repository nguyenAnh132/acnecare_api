package com.acnecare.api.message.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.acnecare.api.common.dto.ApiResponse;
import com.acnecare.api.message.dto.request.MessageLatestRequest;
import com.acnecare.api.message.dto.request.MessageRequest;
import com.acnecare.api.message.dto.request.MessgeGetRequest;
import com.acnecare.api.message.dto.response.MessageResponse;
import com.acnecare.api.message.dto.response.PagedResponse;
import com.acnecare.api.message.service.MessageService;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {
    MessageService messageService;
    @Autowired
    private org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate;

    /**
     * Lấy danh sách tin nhắn của phòng chat theo phân trang (load more khi scroll).
     * Tin nhắn mới nhất sẽ ở đầu (DESC theo createAt).
     *
     * @param roomId ID của phòng chat
     * @param page   số trang (0-based)
     * @param size   số tin nhắn mỗi trang (thường 20-30)
     * @return Page<MessageResponse> bọc trong ApiResponse
     */
    @PostMapping("/rooms")
    public ApiResponse<PagedResponse<MessageResponse>> getMessagesByRoomId(
            @Valid @RequestBody MessgeGetRequest message) {

        PagedResponse<MessageResponse> messages = messageService.getMessagesByRoomId(message);

        return ApiResponse.<PagedResponse<MessageResponse>>builder()
                .code(1000)
                .message("SUCCESS")
                .result(messages)
                .build();
    }

    /**
     * Gửi một tin nhắn mới (text, image, file, system...).
     * Sau khi lưu sẽ tự động cập nhật lastMessageAt của phòng chat.
     *
     * @param request Thông tin tin nhắn: roomId, senderId, content, type
     * @return MessageResponse của tin nhắn vừa gửi
     */
    @PostMapping
    public ApiResponse<MessageResponse> sendMessage(
            @Valid @RequestBody MessageRequest request) {

        MessageResponse saved = messageService.saveMessage(request);

        return ApiResponse.<MessageResponse>builder()
                .code(1000)
                .message("MESSAGE_SENT")
                .result(saved)
                .build();
    }

    /**
     * Lấy tin nhắn mới nhất của phòng chat (dùng để hiển thị preview trong danh
     * sách phòng).
     *
     * @param roomId ID của phòng chat
     * @return MessageResponse hoặc null nếu phòng chưa có tin nhắn nào
     */
    @PostMapping("/latest")
    public ApiResponse<List<MessageResponse>> getLatestMessage(@Valid @RequestBody MessageLatestRequest mRequest) {

        List<MessageResponse> latest = messageService.getLatestMessage(mRequest);

        return ApiResponse.<List<MessageResponse>>builder()
                .code(1000)
                .message(latest != null ? "SUCCESS" : "NO_MESSAGES")
                .result(latest)
                .build();
    }

    @PostMapping("/image")
    public ApiResponse<MessageResponse> sendImageMessage(
            @RequestParam("roomId") String roomId,
            @RequestParam("senderId") String senderId,
            @RequestParam("file") MultipartFile file) {

        // 1. Gọi Service để lưu ảnh và DB
        MessageResponse saved = messageService.saveImageMessage(roomId, senderId, file);

        // 2. Bắn tin nhắn qua WebSocket cho các user trong phòng
        messagingTemplate.convertAndSend("/topic/room/" + roomId, saved);

        return ApiResponse.<MessageResponse>builder()
                .code(1000)
                .message("IMAGE_SENT")
                .result(saved)
                .build();
    }
}
