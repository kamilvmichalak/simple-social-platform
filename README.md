# Simple Social Platform

REST API dla prostej platformy społecznościowej umożliwiającej zarządzanie znajomymi, grupami, udostępnianie wiadomości, komentarze i reakcje.

## Technologie

- Java 17
- Spring Boot 3.2.x
- Spring Security + JWT
- Spring Data JPA
- Hibernate
- MySQL 8.0
- Maven

## Funkcjonalności

### Zarządzanie użytkownikami
- Rejestracja użytkownika
- Logowanie i uwierzytelnianie za pomocą JWT
- Aktualizacja profilu użytkownika
- Wyszukiwanie użytkowników

### Zarządzanie znajomymi
- Wysyłanie zaproszeń do znajomych
- Akceptacja/odrzucanie zaproszeń
- Wyświetlanie listy znajomych
- Usuwanie znajomych

### Posty i interakcje
- Tworzenie, edycja i usuwanie postów
- Dodawanie komentarzy do postów
- Dodawanie reakcji (polubienia itp.)
- Wyświetlanie feedu z postami znajomych

### Wiadomości
- Wysyłanie prywatnych wiadomości do znajomych
- Wyświetlanie historii konwersacji
- Oznaczanie wiadomości jako przeczytane
- Usuwanie wiadomości

### Grupy
- Tworzenie i zarządzanie grupami
- Dołączanie do grup
- Publiczne i prywatne grupy
- Zarządzanie członkami grupy i ich rolami

### Panel administratora
- Zarządzanie użytkownikami (blokowanie/odblokowywanie)
- Moderacja treści (usuwanie postów/komentarzy)
- Zarządzanie grupami

## Uruchomienie projektu

### Wymagania

- Java 17
- MySQL 8.0
- Maven

### Konfiguracja bazy danych

1. Utwórz bazę danych MySQL:

```sql
CREATE DATABASE simplesocial;
CREATE USER 'socialuser'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON simplesocial.* TO 'socialuser'@'localhost';
FLUSH PRIVILEGES;
```

2. Skonfiguruj połączenia w pliku `application.properties`

### Budowanie i uruchamianie

```bash
# Klonowanie repozytorium
git clone https://github.com/username/simple-social-platform.git
cd simple-social-platform

# Budowanie projektu
mvn clean install

# Uruchamianie aplikacji
java -jar target/simple-social-platform-0.0.1-SNAPSHOT.jar
```

Alternatywnie, można uruchomić aplikację bezpośrednio za pomocą Maven:

```bash
mvn spring-boot:run
```

Aplikacja domyślnie uruchomi się pod adresem: http://localhost:8080

### Profile uruchomieniowe

Aplikacja posiada dwa profile uruchomieniowe:
- `dev` - do lokalnego developmentu (domyślny)
- `prod` - do środowiska produkcyjnego

```bash
# Uruchomienie z profilem produkcyjnym
java -jar target/simple-social-platform-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## API Documentation

Po uruchomieniu aplikacji, dokumentacja API dostępna jest pod adresem:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Endpointy REST API

### Autentykacja
- `POST /api/auth/register` - Rejestracja nowego użytkownika
- `POST /api/auth/login` - Logowanie użytkownika
- `POST /api/auth/refresh` - Odświeżanie tokenu JWT

### Użytkownicy
- `GET /api/users` - Wyszukiwanie użytkowników
- `GET /api/users/{id}` - Pobieranie szczegółów użytkownika
- `PUT /api/users/{id}` - Aktualizacja profilu użytkownika
- `DELETE /api/users/{id}` - Dezaktywacja konta

### Posty
- `GET /api/posts` - Pobieranie postów z feeda
- `GET /api/posts/{id}` - Pobieranie szczegółów posta
- `POST /api/posts` - Tworzenie nowego posta
- `PUT /api/posts/{id}` - Aktualizacja posta
- `

```
simple-social-platform/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── simplesocial/
│   │   │           ├── config/
│   │   │           │   ├── SecurityConfig.java
│   │   │           │   ├── WebConfig.java
│   │   │           │   └── SwaggerConfig.java
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