# Makpyr Marua Student API

Student: Marua Makpyr, IT2-2306, ID 37578

Status: project structure created

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
