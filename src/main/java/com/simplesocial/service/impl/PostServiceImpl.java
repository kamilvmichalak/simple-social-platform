package com.simplesocial.service.impl;

import com.simplesocial.entity.Post;
import com.simplesocial.entity.User;
import com.simplesocial.exception.ResourceNotFoundException;
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
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    @Override
    @Transactional
    public Post updatePost(Long id, Post postDetails) {
        Post post = findById(id);

        if (postDetails.getContent() != null) {
            post.setContent(postDetails.getContent());
        }
        if (postDetails.getImageUrl() != null) {
            post.setImageUrl(postDetails.getImageUrl());
        }
        if (postDetails.getIsPublic() != post.getIsPublic()) {
            post.setIsPublic(postDetails.getIsPublic());
        }

        return postRepository.save(post);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        Post post = findById(id);
        postRepository.delete(post);
    }

    @Override
    public Page<Post> findByAuthor(User author, Pageable pageable) {
        return postRepository.findByAuthor(author, pageable);
    }

    @Override
    public Page<Post> findFriendsPosts(Iterable<User> friends, Pageable pageable) {
        return postRepository.findFriendsPosts(friends, pageable);
    }

    @Override
    public Page<Post> findByGroupId(Long groupId, Pageable pageable) {
        return postRepository.findByGroupId(groupId, pageable);
    }

    @Override
    public Page<Post> findPublicPosts(Pageable pageable) {
        return postRepository.findPublicPosts(pageable);
    }
}