package com.acnecare.api.chatroom.controller;

import com.acnecare.api.message.dto.request.MessageRequest;
import com.acnecare.api.message.dto.response.MessageResponse;
import com.acnecare.api.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    // Lắng nghe tin nhắn gửi đến endpoint: /app/chat.sendMessage
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageRequest chatMessage) {
        // 1. Lưu tin nhắn vào Database
        MessageResponse savedMsg = messageService.saveMessage(chatMessage);

        // 2. Phát tin nhắn trả lại cho những ai đang ở trong Room này
        messagingTemplate.convertAndSend("/topic/room/" + chatMessage.getRoomId(), savedMsg);
    }
}