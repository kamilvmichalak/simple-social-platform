const API_URL = 'http://localhost:8080/api';

// Login form handler
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const response = await fetch(`${API_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
                credentials: 'include'
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('token', data.token);
                localStorage.setItem('user', JSON.stringify(data));
                window.location.href = '/index.html';
            } else {
                const error = await response.json();
                alert(error.message || 'Login failed');
            }
        } catch (error) {
            console.error('Login error:', error);
            alert('An error occurred during login');
        }
    });
}

// Registration form handler
const registerForm = document.getElementById('registerForm');
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const username = document.getElementById('username').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        if (password !== confirmPassword) {
            alert('Passwords do not match');
            return;
        }

        try {
            const response = await fetch(`${API_URL}/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, email, password }),
                credentials: 'include'
            });

            if (response.ok) {
                alert('Registration successful! Please login.');
                window.location.href = '/pages/login.html';
            } else {
                const error = await response.json();
                alert(error.message || 'Registration failed');
            }
        } catch (error) {
            console.error('Registration error:', error);
            alert('An error occurred during registration');
        }
    });
}

// Check authentication status and update UI
function checkAuthStatus() {
    const token = localStorage.getItem('token');
    const navLinks = document.querySelector('.nav-links');
    
    if (token) {
        navLinks.innerHTML = `
            <a href="index.html">Home</a>
            <a href="pages/feed.html">Feed</a>
            <a href="pages/profile.html">Profile</a>
            <a href="pages/messages.html">Messages</a>
            <a href="pages/groups.html">Groups</a>
            <a href="#" id="logoutBtn">Logout</a>
        `;
        
        // Add logout handler
        document.getElementById('logoutBtn').addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/index.html';
        });
    } else {
        navLinks.innerHTML = `
            <a href="index.html">Home</a>
            <a href="pages/auth/login.html">Login</a>
            <a href="pages/auth/register.html">Register</a>
        `;
    }
}

// Call checkAuthStatus when page loads
document.addEventListener('DOMContentLoaded', checkAuthStatus); 