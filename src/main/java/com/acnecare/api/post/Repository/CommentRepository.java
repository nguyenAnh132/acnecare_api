package com.acnecare.api.post.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acnecare.api.post.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByPostsIdOrderByCreateAtDesc(String postId);
}
