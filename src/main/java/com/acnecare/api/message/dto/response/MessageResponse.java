package com.acnecare.api.message.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    private String messagesId;    
    private String roomId;
    private String senderId;
    private String messageContent;
    private String type;
    private LocalDateTime createAt;   
    private LocalDateTime isReadAt;
}