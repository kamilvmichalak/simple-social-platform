const API_URL = 'http://localhost:8080/api';

// Load posts when the page loads
document.addEventListener('DOMContentLoaded', () => {
    loadPosts();
    setupPostForm();
});

// Load posts from the API
async function loadPosts() {
    try {
        console.log('Loading posts...');
        const response = await fetch(`${API_URL}/posts/feed`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            console.log('Received posts data:', data);
            
            if (data.success && data.data) {
                const posts = data.data.content || [];
                console.log('Posts to display:', posts);
                displayPosts(posts);
            } else {
                console.error('Invalid response format:', data);
                displayPosts([]);
            }
        } else {
            const errorData = await response.json();
            console.error('Failed to load posts:', errorData);
            displayPosts([]);
        }
    } catch (error) {
        console.error('Error loading posts:', error);
        displayPosts([]);
    }
}

// Display posts in the feed
function displayPosts(posts) {
    const feedContainer = document.getElementById('posts-feed');
    feedContainer.innerHTML = '';

    console.log('Displaying posts:', posts);

    if (!Array.isArray(posts)) {
        console.error('Expected an array of posts but got:', posts);
        feedContainer.innerHTML = '<div class="no-posts">Error loading posts</div>';
        return;
    }

    if (posts.length === 0) {
        feedContainer.innerHTML = '<div class="no-posts">No posts to display</div>';
        return;
    }

    posts.forEach(post => {
        console.log('Creating element for post:', post);
        const postElement = createPostElement(post);
        feedContainer.appendChild(postElement);
    });
}

// Create a post element
function createPostElement(post) {
    const postDiv = document.createElement('div');
    postDiv.className = 'post';
    postDiv.innerHTML = `
        <div class="post-header">
            <img src="${post.author.profilePicture || '../assets/default-avatar.png'}" alt="Profile" class="avatar">
            <div class="post-info">
                <h4>${post.author.username}</h4>
                <span class="post-time">${new Date(post.createdAt).toLocaleString()}</span>
            </div>
        </div>
        <div class="post-content">
            <p>${post.content}</p>
            ${post.imageUrl ? `<img src="${post.imageUrl}" alt="Post image" class="post-image">` : ''}
        </div>
        <div class="post-actions">
            <button class="btn btn-like" data-post-id="${post.id}">
                <span class="like-count">${post.likesCount || 0}</span> Likes
            </button>
            <button class="btn btn-comment" data-post-id="${post.id}">
                <span class="comment-count">${post.commentsCount || 0}</span> Comments
            </button>
        </div>
        <div class="comments-section" id="comments-${post.id}">
            <!-- Comments will be loaded here -->
        </div>
    `;

    // Add event listeners for like and comment buttons
    setupPostInteractions(postDiv, post);

    return postDiv;
}

// Setup post form submission
function setupPostForm() {
    const form = document.getElementById('post-form');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const content = form.querySelector('textarea').value;

        try {
            console.log('Creating post with content:', content);
            const postData = {
                content: content,
                isPublic: true
            };

            console.log('Sending post data:', postData);
            const response = await fetch(`${API_URL}/posts`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(postData)
            });

            if (response.ok) {
                const responseData = await response.json();
                console.log('Post created successfully:', responseData);
                form.reset();
                loadPosts(); // Reload posts after creating a new one
            } else {
                const errorData = await response.json();
                console.error('Failed to create post:', errorData);
                alert('Failed to create post: ' + errorData.message);
            }
        } catch (error) {
            console.error('Error creating post:', error);
            alert('Error creating post. Please try again.');
        }
    });
}

// Setup post interactions (likes and comments)
function setupPostInteractions(postElement, post) {
    const likeButton = postElement.querySelector('.btn-like');
    const commentButton = postElement.querySelector('.btn-comment');

    likeButton.addEventListener('click', async () => {
        try {
            const response = await fetch(`${API_URL}/reactions/posts/${post.id}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ type: 'LIKE' })
            });

            if (response.ok) {
                loadPosts(); // Reload posts to update like count
            }
        } catch (error) {
            console.error('Error liking post:', error);
        }
    });

    commentButton.addEventListener('click', () => {
        const commentsSection = postElement.querySelector(`#comments-${post.id}`);
        if (commentsSection.style.display === 'none') {
            loadComments(post.id, commentsSection);
            commentsSection.style.display = 'block';
        } else {
            commentsSection.style.display = 'none';
        }
    });
}

// Load comments for a post
async function loadComments(postId, commentsSection) {
    try {
        const response = await fetch(`${API_URL}/comments/posts/${postId}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            displayComments(data.data, commentsSection);
        }
    } catch (error) {
        console.error('Error loading comments:', error);
    }
}

// Display comments in the comments section
function displayComments(comments, commentsSection) {
    commentsSection.innerHTML = `
        <div class="comments-list">
            ${comments.map(comment => `
                <div class="comment">
                    <img src="${comment.author.profilePicture || '../assets/default-avatar.png'}" alt="Profile" class="avatar">
                    <div class="comment-content">
                        <h5>${comment.author.username}</h5>
                        <p>${comment.content}</p>
                        <span class="comment-time">${new Date(comment.createdAt).toLocaleString()}</span>
                    </div>
                </div>
            `).join('')}
        </div>
        <form class="comment-form" data-post-id="${comments[0]?.postId}">
            <input type="text" placeholder="Write a comment..." required>
            <button type="submit" class="btn btn-primary">Comment</button>
        </form>
    `;

    // Add event listener for comment form
    const commentForm = commentsSection.querySelector('.comment-form');
    commentForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const content = commentForm.querySelector('input').value;
        const postId = commentForm.dataset.postId;

        try {
            const response = await fetch(`${API_URL}/comments/posts/${postId}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ content })
            });

            if (response.ok) {
                commentForm.reset();
                loadComments(postId, commentsSection);
            }
        } catch (error) {
            console.error('Error posting comment:', error);
        }
    });
} 