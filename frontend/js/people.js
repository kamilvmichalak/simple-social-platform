const API_URL = 'http://localhost:8080/api';

// Load data when the page loads
document.addEventListener('DOMContentLoaded', () => {
    loadFriendRequests();
    loadFriends();
    setupSearch();
});

// Setup search functionality
function setupSearch() {
    const searchButton = document.getElementById('searchButton');
    const searchInput = document.getElementById('searchInput');

    searchButton.addEventListener('click', () => {
        const query = searchInput.value.trim();
        if (query) {
            searchUsers(query);
        }
    });

    searchInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            const query = searchInput.value.trim();
            if (query) {
                searchUsers(query);
            }
        }
    });
}

// Search for users
async function searchUsers(query) {
    try {
        const response = await fetch(`${API_URL}/users/search?query=${encodeURIComponent(query)}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            displaySearchResults(data.data);
        } else {
            console.error('Failed to search users');
        }
    } catch (error) {
        console.error('Error searching users:', error);
    }
}

// Display search results
function displaySearchResults(users) {
    const resultsContainer = document.getElementById('searchResults');
    resultsContainer.innerHTML = '';

    if (users.length === 0) {
        resultsContainer.innerHTML = '<p>No users found</p>';
        return;
    }

    users.forEach(user => {
        const userElement = createUserElement(user, 'search');
        resultsContainer.appendChild(userElement);
    });
}

// Load friend requests
async function loadFriendRequests() {
    try {
        const response = await fetch(`${API_URL}/friendships/requests`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            displayFriendRequests(data.data);
        } else {
            console.error('Failed to load friend requests');
        }
    } catch (error) {
        console.error('Error loading friend requests:', error);
    }
}

// Display friend requests
function displayFriendRequests(requests) {
    const requestsContainer = document.getElementById('friendRequests');
    requestsContainer.innerHTML = '';

    if (requests.length === 0) {
        requestsContainer.innerHTML = '<p>No pending friend requests</p>';
        return;
    }

    requests.forEach(request => {
        const userElement = createUserElement(request.user, 'request');
        requestsContainer.appendChild(userElement);
    });
}

// Load friends
async function loadFriends() {
    try {
        const response = await fetch(`${API_URL}/friendships/friends`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            displayFriends(data.data);
        } else {
            console.error('Failed to load friends');
        }
    } catch (error) {
        console.error('Error loading friends:', error);
    }
}

// Display friends
function displayFriends(friends) {
    const friendsContainer = document.getElementById('friendsList');
    friendsContainer.innerHTML = '';

    if (friends.length === 0) {
        friendsContainer.innerHTML = '<p>No friends yet</p>';
        return;
    }

    friends.forEach(friend => {
        const userElement = createUserElement(friend, 'friend');
        friendsContainer.appendChild(userElement);
    });
}

// Create a user element
function createUserElement(user, type) {
    const userDiv = document.createElement('div');
    userDiv.className = 'user-card';
    userDiv.innerHTML = `
        <img src="${user.profilePicture || '../assets/default-avatar.png'}" alt="Profile" class="avatar">
        <div class="user-info">
            <h4>${user.username}</h4>
            <p>${user.name || ''}</p>
        </div>
        <div class="user-actions">
            ${getActionButton(type, user.id)}
        </div>
    `;

    // Add event listener for the action button
    const actionButton = userDiv.querySelector('.btn');
    if (actionButton) {
        actionButton.addEventListener('click', () => handleUserAction(type, user.id, actionButton));
    }

    return userDiv;
}

// Get the appropriate action button based on the type
function getActionButton(type, userId) {
    switch (type) {
        case 'search':
            return `<button class="btn btn-primary" data-user-id="${userId}">Add Friend</button>`;
        case 'request':
            return `
                <button class="btn btn-success" data-user-id="${userId}">Accept</button>
                <button class="btn btn-danger" data-user-id="${userId}">Reject</button>
            `;
        case 'friend':
            return `<button class="btn btn-danger" data-user-id="${userId}">Unfriend</button>`;
        default:
            return '';
    }
}

// Handle user actions (add friend, accept/reject request, unfriend)
async function handleUserAction(type, userId, button) {
    try {
        let response;
        const token = localStorage.getItem('token');
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };

        switch (type) {
            case 'search':
                response = await fetch(`${API_URL}/friendships/request/${userId}`, {
                    method: 'POST',
                    headers
                });
                if (response.ok) {
                    button.textContent = 'Request Sent';
                    button.disabled = true;
                }
                break;

            case 'request':
                if (button.classList.contains('btn-success')) {
                    response = await fetch(`${API_URL}/friendships/accept/${userId}`, {
                        method: 'POST',
                        headers
                    });
                } else {
                    response = await fetch(`${API_URL}/friendships/reject/${userId}`, {
                        method: 'POST',
                        headers
                    });
                }
                if (response.ok) {
                    loadFriendRequests();
                    loadFriends();
                }
                break;

            case 'friend':
                response = await fetch(`${API_URL}/friendships/${userId}`, {
                    method: 'DELETE',
                    headers
                });
                if (response.ok) {
                    loadFriends();
                }
                break;
        }

        if (!response.ok) {
            const error = await response.json();
            alert(error.message || 'Failed to perform action');
        }
    } catch (error) {
        console.error('Error performing action:', error);
        alert('Error performing action. Please try again.');
    }
} 