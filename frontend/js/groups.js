const API_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    loadGroups();
    loadMyGroups();
    loadSuggestedGroups();
    setupCreateGroupModal();
});

async function loadGroups() {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = 'login.html';
            return;
        }
        const response = await fetch(`${API_URL}/groups`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (!response.ok) {
            throw new Error('Failed to load groups');
        }

        const result = await response.json();
        const groups = result.data || result;

        displayGroups(groups);
    } catch (error) {
        console.error('Error loading groups:', error);
    }
}

function displayGroups(groups) {
    const groupsContainer = document.getElementById('groups-list');
    groupsContainer.innerHTML = '';

    if (!groups || groups.length === 0) {
        groupsContainer.innerHTML = '<p>No groups found</p>';
        return;
    }

    groups.forEach(group => {
        const groupElement = document.createElement('div');
        groupElement.className = 'group-card';
        groupElement.innerHTML = `
            <div class="group-info">
                <h3>${group.name}</h3>
                <p class="group-description">${group.description}</p>
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

        const result = await response.json();
        const groups = result.data || result;
        displayMyGroups(groups);
    } catch (error) {
        console.error('Error loading my groups:', error);
    }
}

function displayMyGroups(groups) {
    const myGroupsContainer = document.getElementById('my-groups');
    if (!myGroupsContainer) {
        return;
    }
    myGroupsContainer.innerHTML = '';

    if (!groups || groups.length === 0) {
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

        const result = await response.json();
        const groups = result.data || result;
        displaySuggestedGroups(groups);
    } catch (error) {
        console.error('Error loading suggested groups:', error);
    }
}

function displaySuggestedGroups(groups) {
    const suggestedGroupsContainer = document.getElementById('suggested-groups');
    suggestedGroupsContainer.innerHTML = '';

    if (!groups || groups.length === 0) {
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
        suggestedGroupsContainer.appendChild(groupElement);
    });
}

function setupCreateGroupModal() {
    const modal = document.getElementById('create-group-modal');
    const createBtn = document.getElementById('create-group-btn');
    const cancelBtn = document.getElementById('cancel-create');
    const form = document.getElementById('create-group-form');

    createBtn.addEventListener('click', () => {
        modal.classList.add('show');
    });

    cancelBtn.addEventListener('click', () => {
        modal.classList.remove('show');
    });

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const name = document.getElementById('group-name').value.trim();
        const description = document.getElementById('group-description').value.trim();
        const token = localStorage.getItem('token');

        try {
            const response = await fetch(`${API_URL}/groups`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name, description })
            });
            if (!response.ok) {
                const err = await response.json().catch(() => null);
                throw new Error(err?.message || 'Failed to create group');
            }

            const result = await response.json();
            if (!result.success) {
                throw new Error(result.message || 'Failed to create group');
            }

            modal.classList.remove('show');
            form.reset();

            // Refresh groups lists
            loadGroups();
            loadMyGroups();

        } catch (error) {
            console.error('Error creating group:', error);
            alert('Failed to create group. Please try again.');
        }
    });
}

function setupSearch() {
    const searchInput = document.getElementById('group-search');
    if (!searchInput) return;
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

                const result = await response.json();
                const groups = result.data || result;
                displayGroups(groups);
            } catch (error) {
                console.error('Error searching groups:', error);
            }
        }, 300);
    });
}

function setupLogout() {
    const logoutBtn = document.getElementById('logout');
    if (!logoutBtn) return;
    logoutBtn.addEventListener('click', (e) => {
        e.preventDefault();
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = 'login.html';
    });
}
