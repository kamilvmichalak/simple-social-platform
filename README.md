# Simple Social Platform

REST API for a simple social platform enabling friend management, groups, messaging, comments, and reactions.

## Technologies

- Java 17
- Spring Boot 3.2.x
- Spring Security + JWT
- Spring Data JPA
- Hibernate
- MySQL 8.0
- Maven

## Quick Start - Running the Application

### Backend Server

1. Navigate to the backend directory:

```bash
cd backend
```

2. Start the Spring Boot server:

```bash
mvn spring-boot:run
```

The backend will run on `http://localhost:8080`

### Frontend Server

1. Open a new terminal window
2. Navigate to the frontend directory:

```bash
cd frontend
```

3. Start the Python HTTP server:

```bash
python3 -m http.server 4200
```

The frontend will be available at `http://localhost:4200`

### Stopping the Servers

- To stop the backend server: Press `Ctrl + C` in the backend terminal
- To stop the frontend server: Press `Ctrl + C` in the frontend terminal

Note: Both servers need to be running simultaneously for the application to work properly.

## Features

### User Management

- User registration
- Login and authentication using JWT
- User profile updates
- User search

### Friend Management

- Sending friend requests
- Accepting/rejecting requests
- Viewing friend lists
- Removing friends

### Posts and Interactions

- Creating, editing, and deleting posts
- Adding comments to posts
- Adding reactions (likes, etc.)
- Displaying a feed with friends' posts

### Messages

- Sending private messages to friends
- Viewing conversation history
- Marking messages as read
- Deleting messages

### Groups

- Creating and managing groups
- Joining groups
- Public and private groups
- Managing group members and their roles

### Admin Panel

- User management (blocking/unblocking)
- Content moderation (removing posts/comments)
- Group management

## Project Setup

### Requirements

- Java 17
- MySQL 8.0
- Maven

2. Configure connections in the `application.properties` file

### Building and Running

```bash
# Clone the repository
git clone https://github.com/username/simple-social-platform.git
cd simple-social-platform

# Build the project
mvn clean install

# Run the application
java -jar target/simple-social-platform-0.0.1-SNAPSHOT.jar
```

Alternatively, you can run the application directly using Maven:

```bash
mvn spring-boot:run
```

The application will run by default at: http://localhost:8080

### Runtime Profiles

The application has two runtime profiles:

- `dev` - for local development (default)
- `prod` - for production environment

```bash
# Running with production profile
java -jar target/simple-social-platform-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## REST API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Refresh JWT token

### Users

- `GET /api/users` - Search users
- `GET /api/users/{id}` - Get user details
- `PUT /api/users/{id}` - Update user profile
- `DELETE /api/users/{id}` - Deactivate account

### Posts

- `GET /api/posts` - Get posts from feed
- `GET /api/posts/{id}` - Get post details
- `POST /api/posts` - Create a new post
- `PUT /api/posts/{id}` - Update a post

## Database Access

The application uses a MySQL database hosted at the university. To view the database contents:

1. Connect to the MySQL database:

```bash
mysql -h 150.254.36.243 -u ait127869 -p
```

When prompted, enter the password: `--------`

2. Select the database:

```sql
USE ait127869;
```

3. View user accounts:

```sql
SELECT id, username, email, created_at FROM users;
```

4. View posts:

```sql
SELECT * FROM posts;
```

5. View comments:

```sql
SELECT * FROM comments;
```

6. View groups:

```sql
SELECT * FROM groups;
```

## API Endpoints

### Authentication

- POST `/api/auth/register` - Register a new user
- POST `/api/auth/login` - Login user

### Posts

- GET `/api/posts` - Get all posts
- POST `/api/posts` - Create a new post
- GET `/api/posts/{id}` - Get a specific post
- PUT `/api/posts/{id}` - Update a post
- DELETE `/api/posts/{id}` - Delete a post

### Comments

- GET `/api/comments/posts/{postId}` - Get comments for a post
- POST `/api/comments/posts/{postId}` - Add a comment to a post
- DELETE `/api/comments/{id}` - Delete a comment

### Groups

- GET `/api/groups` - Get all groups
- POST `/api/groups` - Create a new group
- GET `/api/groups/{id}` - Get a specific group
- POST `/api/groups/{id}/join` - Join a group
- DELETE `/api/groups/{id}/leave` - Leave a group

### Messages

- GET `/api/messages` - Get all messages
- POST `/api/messages` - Send a message
- GET `/api/messages/{id}` - Get a specific message
- DELETE `/api/messages/{id}` - Delete a message

## Security

The application uses JWT (JSON Web Tokens) for authentication. All requests to protected endpoints must include the JWT token in the Authorization header:

```
Authorization: Bearer <token>
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details

```
simple-social-platform/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── simplesocial/
│   │   │           ├── config/
│   │   │           │   ├── SecurityConfig.java
│   │   │           │   └── WebConfig.java
│   │   │           ├── controller/
│   │   │           │   ├── AuthController.java
│   │   │           │   ├── UserController.java
│   │   │           │   ├── PostController.java
│   │   │           │   ├── CommentController.java
│   │   │           │   ├── FriendshipController.java
│   │   │           │   ├── GroupController.java
│   │   │           │   ├── MessageController.java
│   │   │           │   └── AdminController.java
│   │   │           ├── dto/
│   │   │           │   ├── request/
│   │   │           │   │   ├── UserRegistrationRequest.java
│   │   │           │   │   ├── LoginRequest.java
│   │   │           │   │   ├── PostRequest.java
│   │   │           │   │   └── ...
│   │   │           │   └── response/
│   │   │           │       ├── UserResponse.java
│   │   │           │       ├── PostResponse.java
│   │   │           │       ├── ApiResponse.java
│   │   │           │       └── ...
│   │   │           ├── entity/
│   │   │           │   ├── User.java
│   │   │           │   ├── Post.java
│   │   │           │   ├── Comment.java
│   │   │           │   ├── Friendship.java
│   │   │           │   ├── FriendRequest.java
│   │   │           │   ├── Group.java
│   │   │           │   ├── GroupMember.java
│   │   │           │   ├── Message.java
│   │   │           │   └── Reaction.java
│   │   │           ├── exception/
│   │   │           │   ├── ResourceNotFoundException.java
│   │   │           │   ├── GlobalExceptionHandler.java
│   │   │           │   └── ...
│   │   │           ├── repository/
│   │   │           │   ├── UserRepository.java
│   │   │           │   ├── PostRepository.java
│   │   │           │   ├── CommentRepository.java
│   │   │           │   ├── FriendshipRepository.java
│   │   │           │   ├── FriendRequestRepository.java
│   │   │           │   ├── GroupRepository.java
│   │   │           │   ├── GroupMemberRepository.java
│   │   │           │   ├── MessageRepository.java
│   │   │           │   └── ReactionRepository.java
│   │   │           ├── service/
│   │   │           │   ├── UserService.java
│   │   │           │   ├── PostService.java
│   │   │           │   ├── CommentService.java
│   │   │           │   ├── FriendshipService.java
│   │   │           │   ├── GroupService.java
│   │   │           │   ├── MessageService.java
│   │   │           │   ├── ReactionService.java
│   │   │           │   └── AdminService.java
│   │   │           ├── util/
│   │   │           │   ├── JwtTokenUtil.java
│   │   │           │   └── ...
│   │   │           └── SimpleSocialApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/
│           └── com/
│               └── simplesocial/
│                   ├── controller/
│                   ├── service/
│                   └── repository/
├── pom.xml
└── README.md
```
