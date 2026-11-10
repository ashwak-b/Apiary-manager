# 🐝 Apiary Manager – Spring Boot REST API

A REST API for managing beekeeping operations: apiaries, hives, health inspections, and honey harvests.
Project for **DS1 – Web Development (Spring Boot)**, ESPRIT School of Business.

## Team Members
- Malek Jayari
- Eya Abdelhamid
- Ashwak Etter
- Ghada Mhirsi

## Business context
A beekeeper owns several **apiaries** (bee yards). Each apiary contains **hives**. Hives are regularly
**inspected** (health tracking) and **harvested** (honey production). Apiaries are also linked to the
**flora** growing nearby, which influences the honey produced.

## Tech stack
Java 21+ · Spring Boot · Spring Data JPA (Hibernate) · MySQL · Bean Validation · Lombok · Maven · Postman

## Data model (6 entities)

| Entity | Key fields | Relationships |
|---|---|---|
| Beekeeper | fullName, email (unique), phone | 1 → N Apiary |
| Apiary | name, location | N → 1 Beekeeper · N ↔ N Flora · 1 → N Hive |
| Flora | name (unique), bloomingSeason | N ↔ N Apiary |
| Hive | code (unique), status, installDate | N → 1 Apiary · 1 → N Inspection · 1 → N Harvest |
| Inspection | inspectionDate, healthLevel, notes | N → 1 Hive |
| Harvest | harvestDate, quantityKg, honeyType | N → 1 Hive |

Enums: `HiveStatus` (ACTIVE, WEAK, QUEENLESS, DEAD), `HealthLevel` (GOOD, FAIR, POOR), `Season`.

## Architecture
`Controller → Service → Repository → MySQL`
- **controller**: receives HTTP requests, validates input (`@Valid`), returns proper status codes
- **service**: business logic and error handling
- **repository**: Spring Data JPA interfaces and custom queries
- **model**: JPA entities
- **exception**: custom exceptions and a global `@RestControllerAdvice` handler

## Business rules
- A hive can only be created inside an existing apiary
- A dead hive cannot be inspected or harvested
- A `POOR` inspection automatically changes an `ACTIVE` hive to `WEAK`
- Beekeeper emails, flora names, and hive codes must be unique

## REST endpoints
Every resource (`/api/beekeepers`, `/api/apiaries`, `/api/flora`, `/api/hives`, `/api/inspections`, `/api/harvests`) supports:

| Method | URL | Description | Success |
|---|---|---|---|
| GET | `/api/{resource}` | List all | 200 |
| GET | `/api/{resource}/{id}` | Get one | 200 |
| POST | `/api/{resource}` | Create | 201 |
| PUT | `/api/{resource}/{id}` | Update | 200 |
| DELETE | `/api/{resource}/{id}` | Delete | 204 |

### Custom queries and extra endpoints
| # | Endpoint | Purpose |
|---|---|---|
| 1 | `GET /api/apiaries/flora/{floraName}` | Apiaries located near a given flower |
| 2 | `GET /api/hives/status/{status}` | Hives by status (e.g. QUEENLESS) |
| 3 | `GET /api/hives/not-inspected?since=yyyy-MM-dd` | Hives with no inspection since a date |
| 4 | `GET /api/inspections/hive/{hiveId}` | Inspection history of a hive, newest first |
| 5 | `GET /api/harvests/between?start=...&end=...` | Harvests in a date range |
| 6 | `GET /api/harvests/total/apiary/{apiaryId}` | Total honey (kg) produced by an apiary |
| – | `PUT /api/apiaries/{apiaryId}/flora/{floraId}` | Link a flora to an apiary (many-to-many) |

## Error handling
| Status | Situation |
|---|---|
| 400 | Validation failure, malformed JSON, invalid enum or date, business rule violation |
| 404 | Resource not found |
| 409 | Duplicate value (email, hive code, flora name) or record still referenced |

Example error response:
```json
{
  "timestamp": "2026-11-09T17:02:11",
  "status": 400,
  "error": "Bad Request",
  "message": { "email": "Email format is invalid" }
}
```

## How to run
1. Install Java 21+ and MySQL.
2. Log in to MySQL and create the database:
   ```sql
   CREATE DATABASE apiary_db;
```
3. Set environment variables `DB_USERNAME` and `DB_PASSWORD` (defaults: `apiary_user` / `change_me`).
4. Run `ApiaryManagerApplication` from Spring Tools, or `./mvnw spring-boot:run`.
5. The API is available at `http://localhost:8082`. Tables are created automatically (`ddl-auto=update`).

## Testing with Postman
Screenshots of successful tests are in `docs/postman/`.
