package com.acnecare.api.post.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acnecare.api.common.exception.AppException;
import com.acnecare.api.common.exception.ErrorCode;
import com.acnecare.api.common.helper.CurrentUserId;
import com.acnecare.api.post.Repository.CommentRepository;
import com.acnecare.api.post.Repository.LikesRepository;
import com.acnecare.api.post.Repository.PostsImagesRepository;
import com.acnecare.api.post.Repository.PostsRepository;
import com.acnecare.api.post.dto.Request.PostsRequest;
import com.acnecare.api.post.dto.Response.CommentResponse;
import com.acnecare.api.post.dto.Response.PostsImageResponse;
import com.acnecare.api.post.dto.Response.PostsResponse;
import com.acnecare.api.post.dto.Response.UserPostsResponse;
import com.acnecare.api.post.entity.Comment;
import com.acnecare.api.post.entity.Posts;
import com.acnecare.api.post.entity.PostsImages;
import com.acnecare.api.post.mapper.CommentMapper;
import com.acnecare.api.post.mapper.PostsImagesMapper;
import com.acnecare.api.post.mapper.PostsMapper;
import com.acnecare.api.user.entity.User;
import com.acnecare.api.user.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostsService {

    PostsRepository postsRepository;
    PostsImagesRepository postsImagesRepository;
    UserRepository userRepository;
    PostsMapper postsMapper;
    PostsImagesMapper postsImagesMapper;
    LikesRepository likesRepository;
    CommentRepository commentRepository;
    CommentMapper commentMapper;

    // Lấy ảnh bài viết
    private List<PostsImageResponse> getPostImages(String postId) {
        List<PostsImages> postsImagesList = postsImagesRepository.findByPostsId(postId);
        return postsImagesList.stream()
                .map(postsImagesMapper::toPostsImageResponse)
                .toList();
    }

    // 
    private void enrichPostWithLikeInfo(PostsResponse response, String postID){
        long totalLikes = likesRepository.countByPostsId(postID);
        response.setLikesCount(totalLikes);
        try {
            var userId = CurrentUserId.getCurrentUserId().orElse(null);
            if (userId != null) {
                boolean isLiked = likesRepository.existsByPostsIdAndUserId(postID, userId);
                response.setLiked(isLiked);
            } else {
                response.setLiked(false);
            }
        } catch (Exception e) {
            response.setLiked(false);
        }
    }

    //
    private void enrichPostWithCommentInfo(PostsResponse response, String postId){
        List<Comment> comments = commentRepository.findByPostsIdOrderByCreateAtDesc(postId);
        
        List<CommentResponse> commentResponses = comments.stream()
                .map(commentMapper::toCommentResponse)
                .toList();
                
        response.setComments(commentResponses);
        response.setCommentsCount(commentResponses.size());
    }
    
    // Lấy tất cả bài viết
    @Transactional(readOnly = true)
    public List<PostsResponse> getAllPosts() {
        List<Posts> postsList = postsRepository.findAll();
        return postsList.stream()
                .map((post) -> {
                    PostsResponse response = postsMapper.toPostsResponse(post);
                    User user = post.getUser();
                    if (user != null) {
                        response.setUser(UserPostsResponse.builder()
                                .id(user.getId())
                                .role(postsMapper.toRoleResponseSet(user.getRoles()))
                                .name(user.getFirstName() + " " + user.getLastName())
                                .avatarUrl(user.getAvatarUrl())
                                .build());
                    }
                    response.setPostsImage(getPostImages(post.getId()));
                    enrichPostWithLikeInfo(response, post.getId());
                    enrichPostWithCommentInfo(response, post.getId());
                    return response;
                })
                .toList();
    }
    // làm lazy loading cho user trong posts response

    // Lấy danh sách bài viết theo userId
    public List<PostsResponse> getPostsByUserId(String userId) {
        var isValidUser = userRepository.existsById(userId);
        if (!isValidUser) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        List<Posts> postsList = postsRepository.findByUserId(userId);
        return postsList.stream()
                .map((post) -> {
                    PostsResponse response = postsMapper.toPostsResponse(post);
                    User user = post.getUser();
                    if (user != null) {
                        response.setUser(UserPostsResponse.builder()
                                .id(user.getId())
                                .role(postsMapper.toRoleResponseSet(user.getRoles()))
                                .name(user.getFirstName() + " " + user.getLastName())
                                .avatarUrl(user.getAvatarUrl())
                                .build());
                    }
                    response.setPostsImage(getPostImages(post.getId()));
                    enrichPostWithLikeInfo(response, post.getId());
                    enrichPostWithCommentInfo(response, post.getId());
                    return response;

                }).toList();
    }
    // làm lazy loading cho user trong posts response
    // Lấy chi tiết bài viết
    public PostsResponse getPostById(String postId) {
        Posts post = postsRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        PostsResponse response = postsMapper.toPostsResponse(post);
        User user = post.getUser();
        if (user != null) {
            response.setUser(UserPostsResponse.builder()
                    .id(user.getId())
                    .role(postsMapper.toRoleResponseSet(user.getRoles()))
                    .name(user.getFirstName() + " " + user.getLastName())
                    .avatarUrl(user.getAvatarUrl())
                    .build());
        }
        // Có thể thay postId vào post.getId() không nhỉ ?????
        response.setPostsImage(getPostImages(post.getId()));
        enrichPostWithLikeInfo(response, post.getId());
        enrichPostWithCommentInfo(response, post.getId());
        return response;
    }
    // Tạo bài viết mới
    public PostsResponse createPost(String userId, PostsRequest request){
        var isValidUser = userRepository.existsById(userId);
        if (!isValidUser){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        Posts posts = postsMapper.toPosts(request);
        posts.setUser(userRepository.getReferenceById(userId));
        posts.setCreatedAt(LocalDateTime.now());
        posts.setUpdatedAt(LocalDateTime.now());
        posts.setStatus(request.getStatus());

        return postsMapper.toPostsResponse(postsRepository.save(posts));
    }
    // Cập nhật bài viết
    // Thêm để kiểm tra AccessDenied nếu userId không khớp với userId của bài viết, hoặc có thể kiểm tra trong controller bằng cách lấy userId từ token và so sánh với userId trong request body
    // @PostAuthorize("returnObject.user.name == authentication.name")
    public PostsResponse updatePost(String userId, String postId, PostsRequest request) {
        var isValidUser = userRepository.existsById(userId);
        if (!isValidUser) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if(userId != null && !posts.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        posts.setPostTitle(request.getPostTitle());
        posts.setPostContent(request.getPostContent());
        posts.setStatus(request.getStatus());
        posts.setUpdatedAt(LocalDateTime.now());

        return postsMapper.toPostsResponse(postsRepository.save(posts));
    }
    // Xóa bài viết
    // @PostAuthorize("returnObject.user.name == authentication.name")
    public void deletePost(String userId, String postId) {
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        if (userId != null && !posts.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        postsRepository.delete(posts);
    }
}
