const API_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    loadGroups();
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
        const data = await response.json();
        const groups = data.data || [];
        displayGroups(groups);
    } catch (error) {
        console.error('Error loading groups:', error);
    }
}

function displayGroups(groups) {
    const container = document.getElementById('groups-list');
    container.innerHTML = '';
    if (!groups.length) {
        container.innerHTML = '<p>No groups found</p>';
        return;
    }
    groups.forEach(g => {
        const div = document.createElement('div');
        div.className = 'group-card';
        div.innerHTML = `
            <div class="group-info">
                <h3>${g.name}</h3>
                <p class="group-description">${g.description}</p>
            </div>
        `;
        container.appendChild(div);
    });
}

function setupCreateGroupModal() {
    const modal = document.getElementById('create-group-modal');
    const openBtn = document.getElementById('create-group-btn');
    const cancelBtn = document.getElementById('cancel-create');
    const form = document.getElementById('create-group-form');

    openBtn.addEventListener('click', () => modal.classList.add('show'));
    cancelBtn.addEventListener('click', () => modal.classList.remove('show'));

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
                throw new Error('Failed to create group');
            }
            modal.classList.remove('show');
            form.reset();
            loadGroups();
        } catch (error) {
            console.error('Error creating group:', error);
            alert('Failed to create group. Please try again.');
        }
    });
}
