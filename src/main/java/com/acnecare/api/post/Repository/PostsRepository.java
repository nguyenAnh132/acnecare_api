package com.acnecare.api.post.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acnecare.api.post.entity.Posts;

@Repository
public interface PostsRepository extends JpaRepository<Posts, String> {
    List<Posts> findByUserId(String userId);
}
