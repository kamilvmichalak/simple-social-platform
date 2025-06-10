const API_URL = 'http://localhost:8080/api';

// Load data when the page loads
document.addEventListener('DOMContentLoaded', () => {
    loadUsers();
    setupSearch();
});

// Setup search functionality
function setupSearch() {
    const searchButton = document.getElementById('searchButton');
    const searchInput = document.getElementById('searchInput');

    if (searchButton && searchInput) {
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
}

// Search for users
async function searchUsers(query) {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = '/login.html';
            return;
        }

        const response = await fetch(`${API_URL}/users/search?query=${encodeURIComponent(query)}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            if (data.success) {
                displaySearchResults(data.data);
            }
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
    if (!resultsContainer) return;

    resultsContainer.innerHTML = '';

    if (!users || users.length === 0) {
        resultsContainer.innerHTML = '<p>No users found</p>';
        return;
    }

    users.forEach(user => {
        const userElement = createUserElement(user);
        resultsContainer.appendChild(userElement);
    });
}

// Create a user element
function createUserElement(user) {
    const userDiv = document.createElement('div');
    userDiv.className = 'user-card';
    userDiv.innerHTML = `
        <div class="user-info">
            <img src="${user.profilePicture || 'https://via.placeholder.com/50'}" alt="Profile" class="avatar">
            <div>
                <h4>${user.username}</h4>
                <p>${user.email}</p>
            </div>
        </div>
        <div class="user-actions">
            <button class="btn btn-primary follow-btn" data-user-id="${user.id}" data-following="0">
                <i class="fas fa-user-plus"></i> Follow
            </button>
        </div>
    `;

    // Add event listener for follow button
    const followButton = userDiv.querySelector('.follow-btn');
    followButton.addEventListener('click', () => handleFollow(user.id, followButton));

    return userDiv;
}

// Load all users
async function loadUsers() {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = '/login.html';
            return;
        }

        // Get current user's friends
        const friendsResponse = await fetch(`${API_URL}/friendships/user/me/friends`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        const friendsData = await friendsResponse.json();
        console.log('Friends data:', friendsData); // Debug log

        const friends = friendsData.data || [];
        console.log('Friends list:', friends); // Debug log

        // Get all users
        const response = await fetch(`${API_URL}/users`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        const data = await response.json();
        console.log('Users data:', data); // Debug log
        
        if (data.success) {
            const users = data.data.content || []; // Get the content array from the paginated response
            const searchResults = document.getElementById('searchResults');
            if (!searchResults) return;

            searchResults.innerHTML = '';

            if (users.length === 0) {
                searchResults.innerHTML = '<p>No users found</p>';
                return;
            }

            users.forEach(user => {
                // Check if user is in friends list
                const isFriend = friends.some(friend => friend.id === user.id);
                console.log(`User ${user.username} isFriend:`, isFriend); // Debug log
                const userElement = createUserElement(user);
                if (isFriend) {
                    const followButton = userElement.querySelector('.follow-btn');
                    followButton.dataset.following = '1';
                    followButton.innerHTML = '<i class="fas fa-user-check"></i> Following';
                    followButton.classList.remove('btn-primary');
                    followButton.classList.add('btn-success');
                }
                searchResults.appendChild(userElement);
            });
        } else {
            console.error('Error loading users:', data.message);
            const searchResults = document.getElementById('searchResults');
            if (searchResults) {
                searchResults.innerHTML = '<p>Error loading users</p>';
            }
        }
    } catch (error) {
        console.error('Error loading users:', error);
        const searchResults = document.getElementById('searchResults');
        if (searchResults) {
            searchResults.innerHTML = '<p>Error loading users</p>';
        }
    }
}

// Handle follow button click
async function handleFollow(userId, button) {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/login.html';
        return;
    }

    try {
        const isFollowing = button.dataset.following === '1';
        console.log('Current following state:', isFollowing); // Debug log

        let response;
        if (isFollowing) {
            // Unfollow
            response = await fetch(`${API_URL}/friendships/user/${userId}/unfollow`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
        } else {
            // Follow
            response = await fetch(`${API_URL}/friendships/user/${userId}/follow`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
        }

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to follow/unfollow user');
        }

        // Update button state
        const newState = !isFollowing;
        button.dataset.following = newState ? '1' : '0';
        button.innerHTML = `<i class="fas ${newState ? 'fa-user-check' : 'fa-user-plus'}"></i> ${newState ? 'Following' : 'Follow'}`;
        button.classList.toggle('btn-primary', !newState);
        button.classList.toggle('btn-success', newState);
        
        console.log('Updated following state:', button.dataset.following); // Debug log

        // Don't reload the users list immediately
        // Instead, update the button state locally
        const userCard = button.closest('.user-card');
        if (userCard) {
            const username = userCard.querySelector('h4').textContent;
            console.log(`Updated button state for user ${username}:`, newState);
        }
    } catch (error) {
        console.error('Error following/unfollowing user:', error);
        alert(error.message || 'Failed to follow/unfollow user. Please try again.');
    }
}