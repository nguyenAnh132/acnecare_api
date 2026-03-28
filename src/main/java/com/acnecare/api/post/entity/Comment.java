package com.acnecare.api.post.entity;

import java.time.LocalDateTime;
import com.acnecare.api.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String commentContent;
    LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "postId", referencedColumnName = "id")
    Posts posts;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id" )
    User user;
}
