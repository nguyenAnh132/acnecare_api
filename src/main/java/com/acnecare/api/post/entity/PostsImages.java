package com.acnecare.api.post.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post_images")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostsImages {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String imageUrl;
    LocalDateTime  createdAt;
    LocalDateTime  updatedAt;

    @ManyToOne
    @JoinColumn(name = "postId")
    Posts posts;
}
