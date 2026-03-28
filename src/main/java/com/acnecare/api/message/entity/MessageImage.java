package com.acnecare.api.message.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "message_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;          

    @Column(name = "thumbnail_url", length = 1000)
    private String thumbnailUrl;       // ảnh nhỏ (optional)

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;             // bytes

    @Column(name = "mime_type")
    private String mimeType;           // image/jpeg, image/png...

    @Column(name = "upload_at")
    private LocalDateTime uploadAt = LocalDateTime.now();

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}