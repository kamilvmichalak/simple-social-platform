package com.simplesocial.service.impl;

import com.simplesocial.dto.request.PostRequest;
import com.simplesocial.dto.response.PostResponse;
import com.simplesocial.dto.response.UserResponse;
import com.simplesocial.entity.Post;
import com.simplesocial.entity.User;
import com.simplesocial.exception.ResourceNotFoundException;
import com.simplesocial.exception.UnauthorizedException;
import com.simplesocial.repository.PostRepository;
import com.simplesocial.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    @Transactional
    public PostResponse createPost(PostRequest postRequest, User currentUser) {
        Post post = new Post();
        post.setContent(postRequest.getContent());
        post.setImageUrl(postRequest.getImageUrl());
        post.setIsPublic(postRequest.getIsPublic());
        post.setAuthor(currentUser);

        Post savedPost = postRepository.save(post);
        return mapToResponse(savedPost);
    }

    @Override
    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return mapToResponse(post);
    }

    @Override
    @Transactional
    public PostResponse updatePost(Long id, PostRequest postRequest, User currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        if (!post.getAuthor().equals(currentUser)) {
            throw new UnauthorizedException("You are not authorized to update this post");
        }

        if (postRequest.getContent() != null) {
            post.setContent(postRequest.getContent());
        }
        if (postRequest.getImageUrl() != null) {
            post.setImageUrl(postRequest.getImageUrl());
        }
        if (postRequest.getIsPublic() != null) {
            post.setIsPublic(postRequest.getIsPublic());
        }

        Post updatedPost = postRepository.save(post);
        return mapToResponse(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long id, User currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        if (!post.getAuthor().equals(currentUser)) {
            throw new UnauthorizedException("You are not authorized to delete this post");
        }

        postRepository.delete(post);
    }

    @Override
    public Page<PostResponse> findByAuthor(User author, Pageable pageable) {
        return postRepository.findByAuthor(author, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<PostResponse> findFriendsPosts(User currentUser, Pageable pageable) {
        return postRepository.findFriendsPosts(currentUser, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<PostResponse> findByGroupId(Long groupId, Pageable pageable) {
        return postRepository.findByGroupId(groupId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<PostResponse> findPublicPosts(Pageable pageable) {
        return postRepository.findPublicPosts(pageable)
                .map(this::mapToResponse);
    }

    private PostResponse mapToResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setContent(post.getContent());
        response.setImageUrl(post.getImageUrl());
        response.setPublic_(post.getIsPublic());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setAuthor(UserResponse.fromUser(post.getAuthor()));
        response.setLikesCount(post.getLikesCount());
        response.setCommentsCount(post.getCommentsCount());
        return response;
    }
}