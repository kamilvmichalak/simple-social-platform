const API_URL = 'http://localhost:8080/api';
let currentConversation = null;
let messagePollingInterval = null;

document.addEventListener('DOMContentLoaded', () => {
    loadConversations();
    setupMessageForm();
    setupLogout();
    setupSearch();
});

async function loadConversations() {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = 'login.html';
            return;
        }

        const response = await fetch(`${API_URL}/messages/conversations`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load conversations');
        }

        const conversations = await response.json();
        displayConversations(conversations);
    } catch (error) {
        console.error('Error loading conversations:', error);
    }
}

function displayConversations(conversations) {
    const conversationsContainer = document.getElementById('conversations');
    conversationsContainer.innerHTML = '';

    if (conversations.length === 0) {
        conversationsContainer.innerHTML = '<p class="no-conversations">No conversations yet</p>';
        return;
    }

    conversations.forEach(conversation => {
        const conversationElement = document.createElement('div');
        conversationElement.className = 'conversation';
        conversationElement.dataset.conversationId = conversation.id;
        conversationElement.innerHTML = `
            <img src="${conversation.participant.profilePicture || '../assets/default-avatar.png'}" alt="Profile" class="avatar">
            <div class="conversation-info">
                <h4>${conversation.participant.username}</h4>
                <p>${conversation.lastMessage?.content || 'No messages yet'}</p>
            </div>
            <span class="conversation-time">${conversation.lastMessage ? new Date(conversation.lastMessage.createdAt).toLocaleTimeString() : ''}</span>
        `;

        conversationElement.addEventListener('click', () => {
            selectConversation(conversation);
        });

        conversationsContainer.appendChild(conversationElement);
    });
}

async function selectConversation(conversation) {
    currentConversation = conversation;
    
    // Update UI
    document.querySelectorAll('.conversation').forEach(el => {
        el.classList.remove('active');
    });
    document.querySelector(`[data-conversation-id="${conversation.id}"]`).classList.add('active');
    
    document.getElementById('chat-username').textContent = conversation.participant.username;
    document.getElementById('chat-status').textContent = conversation.participant.online ? 'online' : 'offline';
    
    // Load messages
    await loadMessages(conversation.id);
    
    // Start polling for new messages
    if (messagePollingInterval) {
        clearInterval(messagePollingInterval);
    }
    messagePollingInterval = setInterval(() => {
        if (currentConversation) {
            loadMessages(currentConversation.id);
        }
    }, 5000);
}

async function loadMessages(conversationId) {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/messages/${conversationId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load messages');
        }

        const messages = await response.json();
        displayMessages(messages);
    } catch (error) {
        console.error('Error loading messages:', error);
    }
}

function displayMessages(messages) {
    const messagesContainer = document.getElementById('chat-messages');
    messagesContainer.innerHTML = '';

    if (messages.length === 0) {
        messagesContainer.innerHTML = '<p class="no-messages">No messages yet</p>';
        return;
    }

    messages.forEach(message => {
        const messageElement = document.createElement('div');
        messageElement.className = `message ${message.sender.id === getCurrentUserId() ? 'sent' : 'received'}`;
        messageElement.innerHTML = `
            <div class="message-content">${message.content}</div>
            <div class="message-time">${new Date(message.createdAt).toLocaleString()}</div>
        `;
        messagesContainer.appendChild(messageElement);
    });

    // Scroll to bottom
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

function setupMessageForm() {
    const form = document.getElementById('message-form');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        if (!currentConversation) {
            alert('Please select a conversation first');
            return;
        }

        const input = form.querySelector('input');
        const content = input.value.trim();

        if (!content) return;

        try {
            const token = localStorage.getItem('token');
            const response = await fetch(`${API_URL}/messages/${currentConversation.id}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ content })
            });

            if (!response.ok) {
                throw new Error('Failed to send message');
            }

            input.value = '';
            await loadMessages(currentConversation.id);
        } catch (error) {
            console.error('Error sending message:', error);
            alert('Failed to send message. Please try again.');
        }
    });
}

function setupSearch() {
    const searchInput = document.getElementById('conversation-search');
    searchInput.addEventListener('input', async (e) => {
        const query = e.target.value.trim();
        
        if (query.length < 2) {
            loadConversations();
            return;
        }

        try {
            const token = localStorage.getItem('token');
            const response = await fetch(`${API_URL}/users/search?query=${encodeURIComponent(query)}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                throw new Error('Failed to search users');
            }

            const users = await response.json();
            displaySearchResults(users);
        } catch (error) {
            console.error('Error searching users:', error);
        }
    });
}

function displaySearchResults(users) {
    const conversationsContainer = document.getElementById('conversations');
    conversationsContainer.innerHTML = '';

    if (users.length === 0) {
        conversationsContainer.innerHTML = '<p class="no-results">No users found</p>';
        return;
    }

    users.forEach(user => {
        const userElement = document.createElement('div');
        userElement.className = 'conversation';
        userElement.innerHTML = `
            <img src="${user.profilePicture || '../assets/default-avatar.png'}" alt="Profile" class="avatar">
            <div class="conversation-info">
                <h4>${user.username}</h4>
                <p>Click to start a conversation</p>
            </div>
        `;

        userElement.addEventListener('click', () => {
            startNewConversation(user);
        });

        conversationsContainer.appendChild(userElement);
    });
}

async function startNewConversation(user) {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_URL}/messages/conversations`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ participantId: user.id })
        });

        if (!response.ok) {
            throw new Error('Failed to create conversation');
        }

        const conversation = await response.json();
        await loadConversations();
        selectConversation(conversation);
    } catch (error) {
        console.error('Error creating conversation:', error);
        alert('Failed to start conversation. Please try again.');
    }
}

function getCurrentUserId() {
    const user = JSON.parse(localStorage.getItem('user'));
    return user?.id;
}

function setupLogout() {
    document.getElementById('logout').addEventListener('click', (e) => {
        e.preventDefault();
        if (messagePollingInterval) {
            clearInterval(messagePollingInterval);
        }
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = 'login.html';
    });
} 