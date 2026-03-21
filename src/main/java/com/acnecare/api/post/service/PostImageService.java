package com.acnecare.api.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acnecare.api.post.Repository.PostsImagesRepository;
import com.acnecare.api.post.dto.Response.PostsImageResponse;
import com.acnecare.api.post.entity.PostsImages;
import com.acnecare.api.post.mapper.PostsImagesMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostImageService {

    PostsImagesRepository postsImagesRepository;
    PostsImagesMapper postsImagesMapper;

    // Lấy danh sách ảnh của bài viết
    @Transactional(readOnly = true)
    public List<PostsImageResponse> getImagesByPostId(String postId) {
        List<PostsImages> postsImagesList = postsImagesRepository.findByPostsId(postId);
        return postsImagesList.stream()
                .map(postsImagesMapper::toPostsImageResponse)
                .toList();
    }
    // Thêm ảnh cho bài viết
    public void addPostImage(String postId, String imageUrl) {
    }
}
