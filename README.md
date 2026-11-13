1. 📄 README.md — Projektbeschreibung

# Todo-API mit Spring Boot

Eine vollständige REST-API für die Verwaltung von Todo-Einträgen —
mit JWT-Authentifizierung, rollenbasierter Autorisierung und PostgreSQL.

## Features

### CRUD-Operationen
- `GET /todos` — alle Todos anzeigen
- `GET /todos/{id}` — ein Todo anzeigen
- `POST /todos` — neues Todo erstellen
- `PUT /todos/{id}` — Todo aktualisieren
- `DELETE /todos/{id}` — Todo löschen

### Authentifizierung
- `POST /auth/login` — Login mit Benutzername + Passwort
- JWT-Token im `Authorization: Bearer <token>`-Header

### Rollen
- `USER` — Zugriff auf `/todos/**`
- `ADMIN` — Zugriff auf `/admin/todos/**` (inkl. `interneNotiz`)

### Admin-Endpunkte
- `GET /admin/todos` — alle Todos mit `interneNotiz`
- `GET /admin/todos/{id}` — ein Todo mit `interneNotiz`

## Technologien

- **Java 21**
- **Spring Boot 4.1.2**
- **Spring Web** — REST-API
- **Spring Data JPA** — Datenbank-Zugriff
- **Spring Security** — Authentifizierung + Autorisierung
- **JWT** (JJWT) — Token-basierte Authentifizierung
- **PostgreSQL 16** — Datenbank
- **H2** — In-Memory-Datenbank für Tests
- **Maven** — Build-Tool
- **JUnit 5** — Tests
- **Docker** — Containerisierung

## Projektstruktur

src/main/java/de/taschenrechner/todoapi/
├── TodoApiApplication.java — Startklasse
├── Todo.java — Entity
├── TodoRepository.java — Repository
├── TodoController.java — REST-Controller
├── controller/
│ ├── AuthController.java — Login
│ └── admin/
│ └── AdminTodoController.java — Admin-Endpunkte
├── dto/ — Data Transfer Objects
│ ├── TodoDTO.java
│ ├── TodoCreateDTO.java
│ ├── TodoUpdateDTO.java
│ ├── AdminTodoDTO.java
│ ├── LoginRequest.java
│ └── LoginResponse.java
├── mapper/
│ └── TodoMapper.java — Entity ↔ DTO
├── exception/
│ ├── TodoNichtGefundenException.java
│ └── GlobalExceptionHandler.java
└── security/
├── SecurityConfig.java
├── JwtService.java
└── JwtAuthenticationFilter.java




## Starten

### Voraussetzungen
- **Java 21**
- **Maven 3.9+**
- **Docker Desktop** (für PostgreSQL)

### 1. PostgreSQL starten

```bash
docker-compose up -d

2. App starten

mvn spring-boot:run
Die App läuft auf http://localhost:8080.

3. Oder: Alles im Container

docker-compose up --build

App + PostgreSQL laufen zusammen in Containern.
mvn test

15 Tests:

TodoMapperTest — 3 Unit-Tests

TodoRepositoryTest — 4 Repository-Tests (@DataJpaTest)

TodoControllerTest — 4 Controller-Tests (@WebMvcTest)

TodoApiIntegrationTest — 3 Integration-Tests (@SpringBootTest)

TodoApiApplicationTests — 1 Kontext-Test


Benutzer
Benutzername		Passwort		Rollen
user				user			USER
admin			admin		USER, ADMIN

Beispiel-Sitzung:
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "user", "password": "user"}'
  
Antwort:
{"token":"eyJhbGciOiJIUzM4NCJ9..."}

2. Todo erstellen
curl -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"titel": "Einkaufen", "erledigt": false}'

Antwort:
{"id":1,"titel":"Einkaufen","erledigt":false}

3. Todos abrufen:
curl http://localhost:8080/todos \
  -H "Authorization: Bearer <token>"

Antwort:
[{"id":1,"titel":"Einkaufen","erledigt":false}]

Architektur:
Client (curl, Postman)
  │
  │  HTTP + Bearer Token
  ▼
Spring Security (JwtAuthenticationFilter)
  │
  ▼
Controller (TodoController, AdminTodoController)
  │
  ▼
Mapper (Entity ↔ DTO)
  │
  ▼
Repository (Spring Data JPA)
  │
  ▼
PostgreSQL (Docker)