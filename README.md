# BevTrace Platform (backend)

REST API of **BevTrace**, an IoT traceability platform for beverage logistics: inventory, dispatch, route traceability, device telemetry,
incidents, analytics and subscriptions. Java 21 · Spring Boot 4 · Spring Data JPA · Spring Security (JWT) · MySQL · OpenAPI.

It serves the Angular frontend (`BevTrace-FrontEnd`) with the same resource names and fields as its `db.json` mock and moves the business rules out
of the browser into domain commands.

## Run it

Requirements: JDK 21, Maven 3.9 and a MySQL server (or Docker).

```bash
mvn spring-boot:run                      # local MySQL (docker compose up -d db), dev profile
docker compose up --build                # MySQL + API in Docker
```

* API `http://localhost:8080/api/v1` · Swagger UI `http://localhost:8080/swagger-ui.html`

### Configuration (environment variables)

| Variable | Default | Purpose |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `dev` | `dev` (MySQL localhost), `prod` |
| `DATABASE_URL` (host), `DATABASE_PORT`, `DATABASE_NAME`, `DATABASE_USER`, `DATABASE_PASSWORD` | `localhost`, `3306`, `bevtrace`, `root`, `root` (dev only; required in prod) | database |
| `JWT_SECRET` | required in prod | JWT signing secret (>= 32 chars); dev uses the value in `application.properties` |
| `CORS_ORIGINS` | `http://localhost:4200` | allowed Angular origins (comma separated) |