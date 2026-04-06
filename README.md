# Users & Profiles REST API

This is a Spring Boot REST API built as part of a Java course. It lets you create, read, update and delete users and their profiles through a REST API.

**completed for the course:** Systematisk programutveckling med Java | CAW24G

---

## What does this project do?

This API manages two things:
- **Users** — people with a username and email address
- **Profiles** — extra information about a user, like a bio and avatar

You can interact with it using tools like Insomnia or Postman, or connect it to a frontend app.

---

## Tech Stack

| Tool              | What it does                                      |
| ----------------- | ------------------------------------------------- |
| Java 21           | The programming language                          |
| Spring Boot 3.4   | Framework that makes building APIs much easier    |
| Spring Data JPA   | Handles saving and loading data from the database |
| H2 (in-memory)    | A simple database that lives in memory while the app runs |
| JUnit 5 + Mockito | Tools for writing and running automated tests     |
| Docker            | Packages the app so it runs anywhere              |
| GitHub Actions    | Automatically runs tests every time code is pushed |

---

## How to Run

### Option 1 — Run locally (requires Java 21 + Maven installed)

```bash
mvn spring-boot:run
```

The app will start at: http://localhost:8080

You can also view the database in your browser at:
http://localhost:8080/h2-console
(JDBC URL: `jdbc:h2:mem:userprofile_db`)

### Option 2 — Run with Docker (requires Docker Desktop)

```bash
docker build -t userprofile-api .
docker run -p 8080:8080 userprofile-api
```

The app will start at: http://localhost:8080

### Run the tests

```bash
mvn test
```

---

## API Reference

These are all the URLs the API responds to.

### Users

| Method | URL | What it does | Response |
| ------ | --- | ------------ | -------- |
| GET | /api/users | Get a list of all users | 200 |
| GET | /api/users/{id} | Get one user by their ID | 200 or 404 |
| POST | /api/users | Create a new user | 201 or 400 |
| PUT | /api/users/{id} | Update an existing user | 200 or 404 |
| DELETE | /api/users/{id} | Delete a user | 204 or 404 |

### Profiles

| Method | URL | What it does | Response |
| ------ | --- | ------------ | -------- |
| GET | /api/profiles | Get all profiles | 200 |
| GET | /api/profiles/{id} | Get one profile by ID | 200 or 404 |
| GET | /api/profiles/user/{userId} | Get the profile for a specific user | 200 or 404 |
| POST | /api/profiles/user/{userId} | Create a profile for a user | 201 or 404 |
| PUT | /api/profiles/{id} | Update a profile | 200 or 404 |
| DELETE | /api/profiles/{id} | Delete a profile | 204 or 404 |

### Example — Create a user (POST /api/users)

Send this JSON in the request body:
```json
{
  "username": "Michiel",
  "email": "michiel@vandergragt.eu"
}
```

---

## How the code is structured

The code is split into three layers, each with one job:

```
Request → Controller → Service → Repository → Database
```

- **Controller** -> the front door to this application.It receives HTTP requests and sends back responses. The controller contains no logic.
- **Service** -> This is the brain of the application. All your business logic lives here. It validates data and throws errors if something is wrong.
- **Repository** -> The repository talks to the database.It also contains no logic.

This separation makes the code easier to understand, read, test, and change.

---

## How errors are handled

When something goes wrong (like a user not being found), the API always returns a clear JSON response instead of a confusing error message:

```json
{
  "timestamp": "2026-04-06T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 99"
}
```

---

## How testing works

This project was built using **TDD (Test Driven Development)**, which means tests were written before the actual code. The process for each feature was:

1. Write a failing test
2. Write the code to make it pass
3. Clean up the code

There are two types of tests:
- **Service tests** -> test the business logic in isolation using fake (mock) data
- **Controller tests** -> test that the API endpoints return the correct HTTP responses

---

## How Docker works here

The Dockerfile uses a two-stage build:
- **Stage 1** -> uses a full Maven image to compile and build the app
- **Stage 2** -> uses a small lightweight image to just run the app

This keeps the final Docker image small (approximately 25% of your application).

---

## Branch Strategy

- `main` -> stable code only. Never commit directly here.
- `development`-> where active development happens. Merged into main when ready.

Commits use the **Conventional Commits** format, for example:
- `feat: add user endpoint`
- `fix: correct validation error message`
- `test: add profile service tests`