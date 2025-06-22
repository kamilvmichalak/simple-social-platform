const API_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    loadProfile();
    setupLogout();
});

async function loadProfile() {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = 'login.html';
            return;
        }

        const response = await fetch(`${API_URL}/users/profile`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load profile');
        }

        const profile = await response.json();
        displayProfile(profile);
        loadUserPosts();
        loadFriends();
    } catch (error) {
        console.error('Error loading profile:', error);
        alert('Failed to load profile. Please try again.');
    }
}

function displayProfile(profile) {
    document.getElementById('profile-username').textContent = profile.username;
    document.getElementById('profile-email').textContent = profile.email;
    document.getElementById('profile-bio').textContent = profile.bio || 'No bio yet';
    document.getElementById('posts-count').textContent = profile.postsCount || 0;
    document.getElementById('friends-count').textContent = profile.friendsCount || 0;

    if (profile.profilePicture) {
        document.querySelector('.profile-picture').src = profile.profilePicture;
        document.querySelector('.avatar').src = profile.profilePicture;
    }
}

async function loadUserPosts() {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/posts/user`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load posts');
        }

        const posts = await response.json();
        displayPosts(posts);
    } catch (error) {
        console.error('Error loading posts:', error);
    }
}

function displayPosts(posts) {
    const postsContainer = document.getElementById('user-posts');
    postsContainer.innerHTML = '';

    if (posts.length === 0) {
        postsContainer.innerHTML = '<p>No posts yet</p>';
        return;
    }

    posts.forEach(post => {
        const postElement = createPostElement(post);
        postsContainer.appendChild(postElement);
        setupPostInteractions(postElement, post);
    });
}

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
            ${post.image ? `<img src="${post.image}" alt="Post image" class="post-image">` : ''}
        </div>
        <div class="post-actions">
            <button class="btn-like" data-post-id="${post.id}">
                <span class="like-count">${post.likesCount || 0}</span> Likes
            </button>
            <button class="btn-comment" data-post-id="${post.id}">
                <span class="comment-count">${post.commentsCount || 0}</span> Comments
            </button>
        </div>
        <div class="comments-section" id="comments-${post.id}"></div>
    `;
    return postDiv;
}

function setupPostInteractions(postElement, post) {
    const likeButton = postElement.querySelector('.btn-like');
    const commentButton = postElement.querySelector('.btn-comment');
    const commentsSection = postElement.querySelector('.comments-section');

    likeButton.addEventListener('click', () => handleLike(post.id, likeButton));
    commentButton.addEventListener('click', () => {
        commentsSection.style.display = commentsSection.style.display === 'none' ? 'block' : 'none';
        if (commentsSection.style.display === 'block') {
            loadComments(post.id, commentsSection);
        }
    });
}

async function handleLike(postId, button) {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/posts/${postId}/like`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to like post');
        }

        const result = await response.json();
        button.querySelector('.like-count').textContent = result.likesCount;
    } catch (error) {
        console.error('Error liking post:', error);
    }
}

async function loadComments(postId, commentsSection) {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/posts/${postId}/comments`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load comments');
        }

        const comments = await response.json();
        displayComments(comments, commentsSection, postId);
    } catch (error) {
        console.error('Error loading comments:', error);
    }
}

function displayComments(comments, commentsSection, postId) {
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
        <form class="comment-form" data-post-id="${postId}">
            <input type="text" placeholder="Write a comment..." required>
            <button type="submit" class="btn">Post</button>
        </form>
    `;

    const commentForm = commentsSection.querySelector('.comment-form');
    commentForm.addEventListener('submit', (e) => handleCommentSubmit(e, postId));
}

async function handleCommentSubmit(event, postId) {
    event.preventDefault();
    const form = event.target;
    const input = form.querySelector('input');
    const content = input.value.trim();

    if (!content) return;

    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/posts/${postId}/comments`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ content })
        });

        if (!response.ok) {
            throw new Error('Failed to post comment');
        }

        input.value = '';
        loadComments(postId, form.parentElement);
    } catch (error) {
        console.error('Error posting comment:', error);
    }
}

async function loadFriends() {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/users/friends`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load friends');
        }

        const friends = await response.json();
        displayFriends(friends);
    } catch (error) {
        console.error('Error loading friends:', error);
    }
}

function displayFriends(friends) {
    const friendsList = document.getElementById('friends-list');
    friendsList.innerHTML = '';

    if (friends.length === 0) {
        friendsList.innerHTML = '<p>No friends yet</p>';
        return;
    }

    friends.forEach(friend => {
        const friendElement = document.createElement('div');
        friendElement.className = 'friend-card';
        friendElement.innerHTML = `
            <img src="${friend.profilePicture || '../assets/default-avatar.png'}" alt="Profile" class="friend-avatar">
            <h4 class="friend-name">${friend.name}</h4>
            <p class="friend-username">@${friend.username}</p>
            <button class="btn-friend friends" data-user-id="${friend.id}">Friends</button>
        `;
        friendsList.appendChild(friendElement);
    });
}

function setupLogout() {
    document.getElementById('logout').addEventListener('click', (e) => {
        e.preventDefault();
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = 'login.html';
    });
} 