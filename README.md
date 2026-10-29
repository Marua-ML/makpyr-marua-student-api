# Makpyr Marua Student API

Student: Marua Makpyr, IT2-2306, ID 37578

Status: CRUD with duplicate email check merged, API documentation with error responses added

## About

REST API for managing students (CRUD). Stack: Java 21, Spring Boot 4.1, PostgreSQL 16,
Docker, Docker Compose and Jenkins.

## Structure

- `scripts/Makpyr_Marua_system_report.sh` - system monitoring script
- `logs/` - script log and log analysis
- `backups/` - README backup
- `src/` - Spring Boot application
- `Dockerfile`, `docker-compose.yml` - containers
- `Jenkinsfile` - CI pipeline

## API

| Method | Path | Result |
|---|---|---|
| GET | `/api/makpyr-marua/students` | 200, list of students |
| GET | `/api/makpyr-marua/students/{id}` | 200 or 404 |
| POST | `/api/makpyr-marua/students` | 201 with Location, 400 on empty fields |
| PUT | `/api/makpyr-marua/students/{id}` | 200 or 404 |
| DELETE | `/api/makpyr-marua/students/{id}` | 204 or 404 |
| GET | `/api/makpyr-marua/info` | 200, project, author, group, student ID, version |

## Run

```bash
cp .env.example .env    # then set your own POSTGRES_PASSWORD
docker compose up -d --build
curl http://localhost:8081/api/makpyr-marua/info
```

## Change: duplicate email check

Author: Marua Makpyr (IT2-2306, ID 37578), branch `feature/makpyr-marua-crud`.

POST and PUT now return **409 Conflict** (ProblemDetail) when the email is already used
by another student. The check ignores letter case. Test: `duplicateEmailIs409`.

## Error responses

Errors are returned as ProblemDetail JSON (`application/problem+json`):

- 400 - empty or invalid fields in POST / PUT
- 404 - `No student with id N`
- 409 - email is already used by another student

## CI/CD

Jenkins Multibranch Pipeline `Makpyr_Marua_Student_API` polls GitHub every 3 minutes
(`pollSCM('H/3 * * * *')`) and runs Checkout, Build, Test, Docker Build and Verify.
