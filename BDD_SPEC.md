# BDD Test Specification — Users & Profiles API

## Background

These scenarios are written in Gherkin-style Given-When-Then format
as described in the BDD methodology (see course slides week 2).
They guided the TDD implementation in this project.

---

## Feature: User Management

### User Story

**As a** system administrator
**I want to** create, read, update and delete user accounts
**So that** I can control who has access to the system

---

### Scenario 1.1 — Create a new user with valid data

- **Given** the API is running
- **When** I POST to `/api/users` with `{"username":"alice","email":"alice@example.com"}`
- **Then** the response status is `201 Created`
- **And** the response body contains the user with an assigned ID

### Scenario 1.2 — Retrieve an existing user

- **Given** a user with ID 1 exists
- **When** I GET `/api/users/1`
- **Then** the response status is `200 OK`
- **And** the response body contains the username and email

### Scenario 1.3 — Retrieve a non-existent user

- **Given** no user with ID 99 exists
- **When** I GET `/api/users/99`
- **Then** the response status is `404 Not Found`
- **And** the response body contains `"message": "User not found with id: 99"`

### Scenario 1.4 — Update a user

- **Given** a user with ID 1 exists
- **When** I PUT `/api/users/1` with a new username
- **Then** the response status is `200 OK`
- **And** the username is updated in the response

### Scenario 1.5 — Delete a user

- **Given** a user with ID 1 exists
- **When** I DELETE `/api/users/1`
- **Then** the response status is `204 No Content`

### Scenario 1.6 — Create user with blank username (validation)

- **Given** the API is running
- **When** I POST `/api/users` with an empty username field
- **Then** the response status is `400 Bad Request`
- **And** the `errors.username` field contains "Username is required"

### Scenario 1.7 — Create user with invalid email

- **Given** the API is running
- **When** I POST `/api/users` with `email: "notanemail"`
- **Then** the response status is `400 Bad Request`

---

## Feature: Profile Management

### User Story

**As a** registered user
**I want to** create and manage my profile
**So that** other users can learn about me

---

### Scenario 2.1 — Create a profile for an existing user

- **Given** a user with ID 1 exists
- **When** I POST to `/api/profiles/user/1` with `{"bio":"I love coding"}`
- **Then** the response status is `201 Created`
- **And** the profile is linked to user with ID 1

### Scenario 2.2 — Get a profile by user ID

- **Given** user 1 has a profile
- **When** I GET `/api/profiles/user/1`
- **Then** the response status is `200 OK`
- **And** the bio is returned in the response

### Scenario 2.3 — Create profile for non-existent user

- **Given** no user with ID 99 exists
- **When** I POST to `/api/profiles/user/99`
- **Then** the response status is `404 Not Found`

### Scenario 2.4 — Update a profile

- **Given** a profile with ID 1 exists
- **When** I PUT `/api/profiles/1` with a new bio
- **Then** the response status is `200 OK`
- **And** the bio is updated in the response

### Scenario 2.5 — Delete a profile

- **Given** a profile with ID 1 exists
- **When** I DELETE `/api/profiles/1`
- **Then** the response status is `204 No Content`