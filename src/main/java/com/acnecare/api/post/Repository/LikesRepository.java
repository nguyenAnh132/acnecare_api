package com.acnecare.api.post.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acnecare.api.post.entity.Likes;

@Repository
public interface LikesRepository extends JpaRepository<Likes, String> {
    // Tìm bản ghi Like dựa vào postId và user Id 
    Optional<Likes> findByPostsIdAndUserId(String postId, String userId);
    // Kiểm tra người dùng đã like bài này chưa
    boolean existsByPostsIdAndUserId(String postId, String userId);
    // Đếm tổng số lượng Like của bài viết
    long countByPostsId(String postId);
}
