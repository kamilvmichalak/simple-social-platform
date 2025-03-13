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

### Database Configuration

1. Create a MySQL database:

```sql
CREATE DATABASE simplesocial;
CREATE USER 'socialuser'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON simplesocial.* TO 'socialuser'@'localhost';
FLUSH PRIVILEGES;
```

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
