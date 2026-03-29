package com.acnecare.api.chatroom.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatRoomResponse {

    String roomId;
    LocalDateTime createdAt;
    LocalDateTime lastMessageAt;
    LocalDateTime updatedAt;

    // User user;
    // User otherUser;
}
