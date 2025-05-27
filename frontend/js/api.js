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
            <a href="pages/posts/feed.html">Feed</a>
            <a href="pages/messages/inbox.html">Messages</a>
            <a href="pages/groups/list.html">Groups</a>
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