package com.acnecare.api.post.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.common.helper.CurrentUserId;
import com.acnecare.api.common.storage.FileStorageService;
import com.acnecare.api.common.storage.FileUploadResponse;
import com.acnecare.api.common.storage.StorageFolder;
import com.acnecare.api.post.Repository.PostsImagesRepository;
import com.acnecare.api.post.Repository.PostsRepository;
import com.acnecare.api.post.dto.Response.PostsImageResponse;
import com.acnecare.api.post.entity.Posts;
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
    PostsRepository postsRepository;
    FileStorageService fileStorageService;

    // Lấy danh sách ảnh của bài viết
    @Transactional(readOnly = true)
    public List<PostsImageResponse> getImagesByPostId(String postId) {
        List<PostsImages> postsImagesList = postsImagesRepository.findByPostsId(postId);
        return postsImagesList.stream()
                .map(postsImagesMapper::toPostsImageResponse)
                .toList();
    }
    // Thêm ảnh cho bài viết
    public List<PostsImageResponse> uploadAndSavePostImages(String postId, List<MultipartFile> files){
        var userId = CurrentUserId.getCurrentUserId()
            .orElseThrow(()-> new AppException(ErrorCode.UNAUTHENTICATED));
        Posts posts = postsRepository.findById(postId)
            .orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND));
        
        List<PostsImages> savImages = new ArrayList<>();
        for (MultipartFile file : files){
            if(file !=null && !file.isEmpty()){
                FileUploadResponse uploadResponse = fileStorageService.store(file, StorageFolder.posts, userId);

                PostsImages postsImages = PostsImages.builder()
                    .imageUrl(uploadResponse.getUrl())
                    .posts(posts)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
                savImages.add(postsImagesRepository.save(postsImages));
            }
        }
        return savImages.stream()
                .map(postsImagesMapper::toPostsImageResponse)
                .toList();
    }

    public void addPostImage(String postId, String imageUrl) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
                
        PostsImages postImage = PostsImages.builder()
                .imageUrl(imageUrl)
                .posts(post)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
                
        postsImagesRepository.save(postImage);
    }
    
    public void deletePostImage(String imageId) {
        var userId = CurrentUserId.getCurrentUserId()
            .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        PostsImages image = postsImagesRepository.findById(imageId)
            .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND)); 

        if (!image.getPosts().getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        fileStorageService.delete(image.getImageUrl());

        postsImagesRepository.delete(image);
    }
}
