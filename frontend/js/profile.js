const API_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'login.html';
        return;
    }
    loadProfile(token);
    loadFollowers(token);
});

async function loadProfile(token) {
    try {
        const response = await fetch(`${API_URL}/users/profile`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('Failed to load profile');
        const data = await response.json();
        const profile = data.data || data;
        displayProfile(profile);
        loadUserPosts(token, profile.id);
    } catch (err) {
        console.error(err);
        alert('Failed to load profile');
    }
}

function displayProfile(profile) {
    document.getElementById('profile-name').textContent = profile.username;
    document.getElementById('profile-username').textContent = profile.email;
    document.getElementById('posts-count').textContent = profile.postsCount || 0;
    document.getElementById('followers-count').textContent = profile.followersCount || 0;
    if (profile.profilePicture) {
        document.getElementById('profile-photo').src = profile.profilePicture;
    }
}

async function loadUserPosts(token, userId) {
    try {
        const response = await fetch(`${API_URL}/posts/user/${userId}`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('Failed to load posts');
        const data = await response.json();
        const posts = data.data?.content || data;
        displayPosts(posts, token);
    } catch (err) {
        console.error(err);
    }
}

function displayPosts(posts, token) {
    const container = document.getElementById('user-posts');
    container.innerHTML = '';
    if (!Array.isArray(posts) || posts.length === 0) {
        container.innerHTML = '<p>No posts yet</p>';
        return;
    }
    posts.forEach(p => container.appendChild(createPostElement(p, token)));
}

function createPostElement(post, token) {
    const div = document.createElement('div');
    div.className = 'post';
    div.innerHTML = `
        <div class="post-header">
            <img src="${post.author.profilePicture || '../assets/default-avatar.png'}" class="avatar" alt="Profile">
            <div class="post-info">
                <h4>${post.author.username}</h4>
                <span class="post-time">${new Date(post.createdAt).toLocaleString()}</span>
            </div>
        </div>
        <div class="post-content">
            <p>${post.content}</p>
            ${post.imageUrl ? `<img src="${post.imageUrl}" class="post-image" alt="Post image">` : ''}
        </div>
        <div class="post-actions">
            <button class="btn btn-danger btn-delete" data-id="${post.id}">Delete</button>
        </div>`;
    div.querySelector('.btn-delete').addEventListener('click', () => deletePost(post.id, token, div));
    return div;
}

async function deletePost(postId, token, element) {
    if (!confirm('Delete this post?')) return;
    try {
        const response = await fetch(`${API_URL}/posts/${postId}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) {
            element.remove();
        }
    } catch (err) {
        console.error('Delete failed', err);
    }
}

async function loadFollowers(token) {
    try {
        const response = await fetch(`${API_URL}/friendships/user/me/followers`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('Failed to load followers');
        const data = await response.json();
        const followers = data.data || data;
        displayFollowers(followers);
    } catch (err) {
        console.error(err);
    }
}

function displayFollowers(list) {
    const container = document.getElementById('followers-list');
    container.innerHTML = '';
    if (!Array.isArray(list) || list.length === 0) {
        container.innerHTML = '<p>No followers yet</p>';
        return;
    }
    list.forEach(f => {
        const card = document.createElement('div');
        card.className = 'friend-card';
        card.innerHTML = `
            <img src="${f.profilePicture || '../assets/default-avatar.png'}" class="friend-avatar" alt="Profile">
            <h4 class="friend-name">${f.username}</h4>
            <p class="friend-username">${f.email}</p>`;
        container.appendChild(card);
    });
}

