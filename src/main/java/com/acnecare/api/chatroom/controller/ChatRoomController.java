package com.acnecare.api.chatroom.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.acnecare.api.chatroom.dto.request.ChatRoomCheckRequest;
import com.acnecare.api.chatroom.dto.request.ChatRoomCreationRequest;
import com.acnecare.api.chatroom.dto.request.ChatRoomGetRequest;
import com.acnecare.api.chatroom.dto.response.ChatRoomResponse;
import com.acnecare.api.chatroom.service.ChatRoomService;
import com.acnecare.api.common.dto.ApiResponse;

@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomController {
    ChatRoomService chatRoomService;

    @PostMapping
    /**
     * Tạo phòng chat 1-1 giữa hai người dùng.
     * Nếu phòng giữa hai user đã tồn tại thì trả về phòng cũ (không tạo mới).
     *
     * @param request chứa senderId và receiverId
     * @return ApiResponse chứa thông tin phòng chat (mới hoặc đã tồn tại)
     */
    ApiResponse<ChatRoomResponse> createChatRoom(@Valid @RequestBody ChatRoomCreationRequest request) {
        return ApiResponse.<ChatRoomResponse>builder()
                .code(1000)
                .message("Room Chat created successfully")
                .result(chatRoomService.createChatRoom(request))
                .build();
    }

    @PostMapping("/support")
    ApiResponse<ChatRoomResponse> createSupportChatRoom() {
        return ApiResponse.<ChatRoomResponse>builder()
                .code(1000)
                .message("Support room created successfully")
                .result(chatRoomService.createSupportChatRoomForCurrentUser())
                .build();
    }

    @PostMapping("/user")
    public ApiResponse<List<ChatRoomResponse>> getUserChatRooms(@Valid @RequestBody ChatRoomGetRequest request) {
        return ApiResponse.<List<ChatRoomResponse>>builder()
                .code(1000)
                .message("Get all chat rooms successfully")
                .result(chatRoomService.getChatRoomsByUserId(request))
                .build();
    }

    @PostMapping("/check-room-access")
    /**
     * Kiểm tra xem user hiện tại có quyền truy cập vào phòng chat hay không.
     *
     * @param request chứa userid và roomid cần kiểm tra
     * @return ApiResponse<Boolean> - true nếu user là thành viên của phòng, false
     *         nếu không
     */
    public ApiResponse<Boolean> checkRoomAccess(@Valid @RequestBody ChatRoomCheckRequest request) {
        boolean allowed = chatRoomService.checkUserInRoom(request);

        return ApiResponse.<Boolean>builder()
                .code(allowed ? 1000 : 4030)
                .message(allowed ? "ACCESS_GRANTED" : "ACCESS_DENIED")
                .result(allowed)
                .build();
    }
}
