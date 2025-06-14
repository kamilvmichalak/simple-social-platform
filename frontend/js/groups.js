const API_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    loadGroups();
    loadMyGroups();
    loadSuggestedGroups();
    setupCreateGroupModal();
    setupSearch();
    setupLogout();
});

async function loadGroups() {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = 'login.html';
            return;
        }

        const response = await fetch(`${API_URL}/groups`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load groups');
        }

        const groups = await response.json();
        displayGroups(groups);
    } catch (error) {
        console.error('Error loading groups:', error);
    }
}

function displayGroups(groups) {
    const groupsContainer = document.getElementById('groups-list');
    groupsContainer.innerHTML = '';

    if (groups.length === 0) {
        groupsContainer.innerHTML = '<p>No groups found</p>';
        return;
    }

    groups.forEach(group => {
        const groupElement = document.createElement('div');
        groupElement.className = 'group-card';
        groupElement.innerHTML = `
            <div class="group-cover"></div>
            <div class="group-info">
                <h3>${group.name}</h3>
                <p class="group-description">${group.description}</p>
                <div class="group-meta">
                    <span class="group-members">${group.membersCount} members</span>
                    <span class="group-privacy">
                        <i class="fas fa-${group.privacy === 'PUBLIC' ? 'globe' : 'lock'}"></i>
                        ${group.privacy.toLowerCase()}
                    </span>
                </div>
                <div class="group-actions">
                    <button class="btn-join ${group.isMember ? 'joined' : ''}" data-group-id="${group.id}">
                        ${group.isMember ? 'Joined' : 'Join Group'}
                    </button>
                </div>
            </div>
        `;

        const joinButton = groupElement.querySelector('.btn-join');
        joinButton.addEventListener('click', () => handleJoinGroup(group.id, joinButton));

        groupsContainer.appendChild(groupElement);
    });
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
        button.textContent = isJoining ? 'Joined' : 'Join Group';
        
        // Refresh groups lists
        loadMyGroups();
        loadGroups();
    } catch (error) {
        console.error('Error handling group membership:', error);
        alert(`Failed to ${isJoining ? 'join' : 'leave'} group. Please try again.`);
    }
}

async function loadMyGroups() {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/groups/my`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load my groups');
        }

        const groups = await response.json();
        displayMyGroups(groups);
    } catch (error) {
        console.error('Error loading my groups:', error);
    }
}

function displayMyGroups(groups) {
    const myGroupsContainer = document.getElementById('my-groups');
    myGroupsContainer.innerHTML = '';

    if (groups.length === 0) {
        myGroupsContainer.innerHTML = '<p>You are not a member of any groups</p>';
        return;
    }

    groups.forEach(group => {
        const groupElement = document.createElement('div');
        groupElement.className = 'my-group-item';
        groupElement.innerHTML = `
            <img src="${group.coverImage || '../assets/default-group.png'}" alt="${group.name}">
            <div class="my-group-info">
                <h4>${group.name}</h4>
                <p>${group.membersCount} members</p>
            </div>
        `;

        groupElement.addEventListener('click', () => {
            window.location.href = `group.html?id=${group.id}`;
        });

        myGroupsContainer.appendChild(groupElement);
    });
}

async function loadSuggestedGroups() {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/groups/suggested`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load suggested groups');
        }

        const groups = await response.json();
        displaySuggestedGroups(groups);
    } catch (error) {
        console.error('Error loading suggested groups:', error);
    }
}

function displaySuggestedGroups(groups) {
    const suggestedGroupsContainer = document.getElementById('suggested-groups');
    suggestedGroupsContainer.innerHTML = '';

    if (groups.length === 0) {
        suggestedGroupsContainer.innerHTML = '<p>No suggested groups</p>';
        return;
    }

    groups.forEach(group => {
        const groupElement = document.createElement('div');
        groupElement.className = 'suggested-group-item';
        groupElement.innerHTML = `
            <img src="${group.coverImage || '../assets/default-group.png'}" alt="${group.name}">
            <div class="suggested-group-info">
                <h4>${group.name}</h4>
                <p>${group.membersCount} members</p>
            </div>
        `;

        groupElement.addEventListener('click', () => {
            window.location.href = `group.html?id=${group.id}`;
        });

        suggestedGroupsContainer.appendChild(groupElement);
    });
}

function setupCreateGroupModal() {
    const modal = document.getElementById('create-group-modal');
    const createBtn = document.getElementById('create-group-btn');
    const closeBtn = document.querySelector('.close-btn');
    const cancelBtn = document.getElementById('cancel-create-group');
    const form = document.getElementById('create-group-form');

    createBtn.addEventListener('click', () => {
        modal.classList.add('show');
    });

    closeBtn.addEventListener('click', () => {
        modal.classList.remove('show');
    });

    cancelBtn.addEventListener('click', () => {
        modal.classList.remove('show');
    });

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const name = document.getElementById('group-name').value;
        const description = document.getElementById('group-description').value;
        const privacyEl = document.getElementById('group-privacy');
        const privacy = privacyEl ? privacyEl.value : 'PUBLIC';

        try {
            const token = localStorage.getItem('token');
            const response = await fetch(`${API_URL}/groups`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name, description, privacy })
            });

            if (!response.ok) {
                throw new Error('Failed to create group');
            }

            const group = await response.json();
            modal.classList.remove('show');
            form.reset();
            
            // Refresh groups lists
            loadGroups();
            loadMyGroups();
            
            // Redirect to the new group
            window.location.href = `group.html?id=${group.id}`;
        } catch (error) {
            console.error('Error creating group:', error);
            alert('Failed to create group. Please try again.');
        }
    });
}

function setupSearch() {
    const searchInput = document.getElementById('group-search');
    let searchTimeout;

    searchInput.addEventListener('input', (e) => {
        const query = e.target.value.trim();
        
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(async () => {
            if (query.length < 2) {
                loadGroups();
                return;
            }

            try {
                const token = localStorage.getItem('token');
                const response = await fetch(`${API_URL}/groups/search?query=${encodeURIComponent(query)}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                if (!response.ok) {
                    throw new Error('Failed to search groups');
                }

                const groups = await response.json();
                displayGroups(groups);
            } catch (error) {
                console.error('Error searching groups:', error);
            }
        }, 300);
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