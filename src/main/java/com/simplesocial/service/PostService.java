package com.simplesocial.service;

import com.simplesocial.entity.Post;
import com.simplesocial.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Post createPost(Post post);

    Post findById(Long id);

    Post updatePost(Long id, Post postDetails);

    void deletePost(Long id);

    Page<Post> findByAuthor(User author, Pageable pageable);

    Page<Post> findFriendsPosts(Iterable<User> friends, Pageable pageable);

    Page<Post> findByGroupId(Long groupId, Pageable pageable);

    Page<Post> findPublicPosts(Pageable pageable);
}