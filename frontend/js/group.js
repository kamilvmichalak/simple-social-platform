const API_URL = 'http://localhost:8080/api';
let currentGroup = null;

document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const groupId = urlParams.get('id');

    if (!groupId) {
        window.location.href = 'groups.html';
        return;
    }

    loadGroup(groupId);
    setupGroupPostForm();
    setupInviteModal();
    setupLogout();
});

async function loadGroup(groupId) {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = 'login.html';
            return;
        }

        const response = await fetch(`${API_URL}/groups/${groupId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load group');
        }

        currentGroup = await response.json();
        displayGroup(currentGroup);
        loadGroupPosts(groupId);
        loadGroupMembers(groupId);
    } catch (error) {
        console.error('Error loading group:', error);
        alert('Failed to load group. Please try again.');
    }
}

function displayGroup(group) {
    document.title = `${group.name} - Simple Social Platform`;
    document.getElementById('group-name').textContent = group.name;
    document.getElementById('group-description').textContent = group.description;
    document.getElementById('group-members-count').textContent = `${group.membersCount} members`;
    document.getElementById('group-privacy').textContent = group.privacy.toLowerCase();
    document.getElementById('group-about').textContent = group.description;

    const joinButton = document.getElementById('join-group-btn');
    joinButton.textContent = group.isMember ? 'Leave Group' : 'Join Group';
    joinButton.classList.toggle('joined', group.isMember);
    joinButton.addEventListener('click', () => handleJoinGroup(group.id, joinButton));

    const inviteButton = document.getElementById('invite-btn');
    inviteButton.style.display = group.isMember ? 'block' : 'none';
}

async function handleJoinGroup(groupId, button) {
    try {
        const token = localStorage.getItem('token');
        const isJoining = !button.classList.contains('joined');
        const method = isJoining ? 'POST' : 'DELETE';

        const response = await fetch(`${API_URL}/groups/${groupId}/members`, {
            method,
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error(`Failed to ${isJoining ? 'join' : 'leave'} group`);
        }

        button.classList.toggle('joined');
        button.textContent = isJoining ? 'Leave Group' : 'Join Group';
        document.getElementById('invite-btn').style.display = isJoining ? 'block' : 'none';
        
        // Refresh group data
        loadGroup(groupId);
    } catch (error) {
        console.error('Error handling group membership:', error);
        alert(`Failed to ${isJoining ? 'join' : 'leave'} group. Please try again.`);
    }
}

async function loadGroupPosts(groupId) {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/groups/${groupId}/posts`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load group posts');
        }

        const posts = await response.json();
        displayGroupPosts(posts);
    } catch (error) {
        console.error('Error loading group posts:', error);
    }
}

function displayGroupPosts(posts) {
    const postsContainer = document.getElementById('group-posts');
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

function setupGroupPostForm() {
    const form = document.getElementById('group-post-form');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        if (!currentGroup) {
            alert('Please select a group first');
            return;
        }

        const textarea = form.querySelector('textarea');
        const content = textarea.value.trim();
        const imageInput = document.getElementById('post-image');
        const image = imageInput.files[0];

        if (!content && !image) return;

        try {
            const token = localStorage.getItem('token');
            const formData = new FormData();
            formData.append('content', content);
            if (image) {
                formData.append('image', image);
            }

            const response = await fetch(`${API_URL}/groups/${currentGroup.id}/posts`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                body: formData
            });

            if (!response.ok) {
                throw new Error('Failed to create post');
            }

            textarea.value = '';
            imageInput.value = '';
            loadGroupPosts(currentGroup.id);
        } catch (error) {
            console.error('Error creating post:', error);
            alert('Failed to create post. Please try again.');
        }
    });
}

async function loadGroupMembers(groupId) {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/groups/${groupId}/members`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load group members');
        }

        const members = await response.json();
        displayGroupMembers(members);
    } catch (error) {
        console.error('Error loading group members:', error);
    }
}

function displayGroupMembers(members) {
    const membersContainer = document.getElementById('group-members');
    membersContainer.innerHTML = '';

    if (members.length === 0) {
        membersContainer.innerHTML = '<p>No members yet</p>';
        return;
    }

    members.forEach(member => {
        const memberElement = document.createElement('div');
        memberElement.className = 'member-item';
        memberElement.innerHTML = `
            <img src="${member.profilePicture || '../assets/default-avatar.png'}" alt="Profile" class="member-avatar">
            <div class="member-info">
                <h4>${member.username}</h4>
                <p>${member.role}</p>
            </div>
        `;
        membersContainer.appendChild(memberElement);
    });
}

function setupInviteModal() {
    const modal = document.getElementById('invite-modal');
    const inviteBtn = document.getElementById('invite-btn');
    const closeBtn = document.querySelector('.close-btn');
    const searchInput = document.getElementById('invite-search');
    let searchTimeout;

    inviteBtn.addEventListener('click', () => {
        modal.classList.add('show');
    });

    closeBtn.addEventListener('click', () => {
        modal.classList.remove('show');
    });

    searchInput.addEventListener('input', (e) => {
        const query = e.target.value.trim();
        
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(async () => {
            if (query.length < 2) {
                document.getElementById('invite-results').innerHTML = '';
                return;
            }

            try {
                const token = localStorage.getItem('token');
                const response = await fetch(`${API_URL}/users/search?query=${encodeURIComponent(query)}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                if (!response.ok) {
                    throw new Error('Failed to search users');
                }

                const users = await response.json();
                displayInviteResults(users);
            } catch (error) {
                console.error('Error searching users:', error);
            }
        }, 300);
    });
}

function displayInviteResults(users) {
    const resultsContainer = document.getElementById('invite-results');
    resultsContainer.innerHTML = '';

    if (users.length === 0) {
        resultsContainer.innerHTML = '<p>No users found</p>';
        return;
    }

    users.forEach(user => {
        const userElement = document.createElement('div');
        userElement.className = 'invite-user';
        userElement.innerHTML = `
            <img src="${user.profilePicture || '../assets/default-avatar.png'}" alt="Profile" class="avatar">
            <div class="invite-user-info">
                <h4>${user.username}</h4>
                <p>${user.email}</p>
            </div>
            <button class="btn-invite" data-user-id="${user.id}">Invite</button>
        `;

        const inviteButton = userElement.querySelector('.btn-invite');
        inviteButton.addEventListener('click', () => handleInvite(user.id, inviteButton));

        resultsContainer.appendChild(userElement);
    });
}

async function handleInvite(userId, button) {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/groups/${currentGroup.id}/invites`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ userId })
        });

        if (!response.ok) {
            throw new Error('Failed to send invite');
        }

        button.classList.add('sent');
        button.textContent = 'Invited';
        button.disabled = true;
    } catch (error) {
        console.error('Error sending invite:', error);
        alert('Failed to send invite. Please try again.');
    }
}

function setupLogout() {
    document.getElementById('logout').addEventListener('click', (e) => {
        e.preventDefault();
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = 'login.html';
    });
} 