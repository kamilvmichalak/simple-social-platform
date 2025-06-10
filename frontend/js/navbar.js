function createNavbar() {
    const navbar = document.createElement('nav');
    navbar.className = 'navbar';
    
    // Determine if we're in the pages directory
    const isInPages = window.location.pathname.includes('/pages/');
    const basePath = isInPages ? '../' : '';
    
    navbar.innerHTML = `
        <div class="nav-brand">
            <a href="${basePath}index.html">Simple Social Platform</a>
        </div>
        <div class="nav-links">
            <a href="${basePath}pages/feed.html" class="nav-item">Feed</a>
            <a href="${basePath}pages/people.html" class="nav-item">People</a>
            <a href="${basePath}pages/messages.html" class="nav-item">Messages</a>
            <a href="${basePath}pages/groups.html" class="nav-item">Groups</a>
            <a href="${basePath}pages/profile.html" class="nav-item">Profile</a>
            <button id="logoutButton" class="btn btn-danger">Logout</button>
        </div>
    `;

    // Add event listener for logout
    const logoutButton = navbar.querySelector('#logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', () => {
            localStorage.removeItem('token');
            window.location.href = `${basePath}pages/login.html`;
        });
    }

    return navbar;
}

// Initialize navbar when the DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    const navbarContainer = document.getElementById('navbar-container');
    if (navbarContainer) {
        const navbar = createNavbar();
        navbarContainer.appendChild(navbar);

        // Highlight current page in navbar
        const currentPage = window.location.pathname.split('/').pop();
        const navItems = navbar.querySelectorAll('.nav-item');
        navItems.forEach(item => {
            if (item.getAttribute('href').includes(currentPage)) {
                item.classList.add('active');
            }
        });
    }
}); 