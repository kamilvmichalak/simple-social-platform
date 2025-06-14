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
            <a href="/index.html">Home</a>
            <a href="/pages/feed.html">Feed</a>
            <a href="/pages/people.html">People</a>
            <a href="/pages/messages.html">Messages</a>
            <a href="/pages/groups.html">Groups</a>
            <a href="/pages/profile.html">Profile</a>
            <a href="#" onclick="logout()">Logout</a>
        `;
    } else {
        navLinks.innerHTML = `
            <a href="/index.html">Home</a>
            <a href="/pages/login.html">Login</a>
            <a href="/pages/register.html">Register</a>
        `;
    }
}

// Logout function
function logout() {
    localStorage.removeItem('token');
    window.location.href = '/index.html';
}

// Initialize navigation
document.addEventListener('DOMContentLoaded', updateNavigation); 
