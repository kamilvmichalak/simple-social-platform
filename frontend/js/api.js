const API_URL = 'http://localhost:8080/api';

// Check if user is logged in
function isLoggedIn() {
    return localStorage.getItem('token') !== null;
}

// Update navigation based on auth status
function updateNavigation() {
    const navLinks = document.querySelector('.nav-links');
    if (isLoggedIn()) {
        navLinks.innerHTML = `
            <a href="index.html">Home</a>
            <a href="pages/feed.html">Feed</a>
            <a href="pages/messages.html">Messages</a>
            <a href="pages/groups.html">Groups</a>
            <a href="#" onclick="logout()">Logout</a>
        `;
    } else {
        navLinks.innerHTML = `
            <a href="index.html" class="active">Home</a>
            <a href="pages/auth/login.html">Login</a>
            <a href="pages/auth/register.html">Register</a>
        `;
    }
}

// Logout function
function logout() {
    localStorage.removeItem('token');
    window.location.href = 'index.html';
}

// Initialize navigation
document.addEventListener('DOMContentLoaded', updateNavigation);

// Helper function to get auth headers
function getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : '',
        'credentials': 'include'
    };
}

// Helper function to handle API responses
async function handleResponse(response) {
    if (response.status === 401) {
        // Token expired or invalid
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/pages/auth/login.html';
        throw new Error('Session expired. Please login again.');
    }
    
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || 'Something went wrong');
    }
    return data;
}

// API functions
async function fetchWithAuth(url, options = {}) {
    const response = await fetch(`${API_URL}${url}`, {
        ...options,
        headers: {
            ...getAuthHeaders(),
            ...options.headers
        }
    });
    return handleResponse(response);
}

// Export API functions
window.api = {
    get: (url) => fetchWithAuth(url),
    post: (url, data) => fetchWithAuth(url, {
        method: 'POST',
        body: JSON.stringify(data)
    }),
    put: (url, data) => fetchWithAuth(url, {
        method: 'PUT',
        body: JSON.stringify(data)
    }),
    delete: (url) => fetchWithAuth(url, {
        method: 'DELETE'
    })
}; 