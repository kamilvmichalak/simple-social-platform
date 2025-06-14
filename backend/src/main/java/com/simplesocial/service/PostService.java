package com.simplesocial.service;

import com.simplesocial.dto.request.PostRequest;
import com.simplesocial.dto.response.PostResponse;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PostResponse createPost(PostRequest postRequest, User currentUser);

    PostResponse findById(Long id);

    PostResponse updatePost(Long id, PostRequest postRequest, User currentUser);

    void deletePost(Long id, User currentUser);

    Page<PostResponse> findByAuthor(User author, Pageable pageable);

    Page<PostResponse> findFriendsPosts(User currentUser, Pageable pageable);

    Page<PostResponse> findByGroupId(Long groupId, Pageable pageable);

    Page<PostResponse> findPublicPosts(Pageable pageable);

    long countByAuthor(User author);
}