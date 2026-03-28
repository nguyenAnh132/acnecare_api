package com.acnecare.api.post.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acnecare.api.post.entity.PostsImages;

@Repository
public interface PostsImagesRepository extends JpaRepository<PostsImages, String> {
    List<PostsImages> findByPostsId(String postsId);
}
