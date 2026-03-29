package com.acnecare.api.message.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.acnecare.api.message.enums.MessageType;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id")
    private String messagesId; // PK

    @Column(name = "user_id", nullable = false)
    private String senderId; // FK → users table

    @Column(name = "room_id", nullable = false)
    private String roomId; // FK → rooms table (hoặc chat_rooms, conversations...)

    @Column(name = "message_content", columnDefinition = "LONGTEXT")
    private String messageContent;

    @Column(name = "is_read_at")
    private LocalDateTime isReadAt; // null = chưa đọc

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type; // TEXT, IMAGE, FILE, SYSTEM, ...

    // Nếu bạn muốn quan hệ 1-n với MessageImage
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageImage> images = new ArrayList<>();

    // Helper method để thêm ảnh
    public void addImage(MessageImage image) {
        images.add(image);
        image.setMessage(this);
    }
}