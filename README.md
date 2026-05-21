# Pulse — Backend

> Monitoring platform backend built with Spring Boot 3.5. Tracks uptime, latency, SSL certificates, and sends email alerts when incidents are detected.

## Tech Stack

- **Java 21** + **Spring Boot 3.5**
- **Spring Security** with JWT authentication
- **Spring Data JPA** + **PostgreSQL** (Supabase)
- **WebSockets** (STOMP) for real-time updates
- **JavaMailSender** for email notifications
- **Springdoc OpenAPI** for API documentation
- **Docker** ready

## Features

- JWT authentication (register, login)
- Project and monitored service management (CRUD)
- Automatic HTTP checks every 60 seconds
- Automatic incident detection (OPEN / RESOLVED)
- SSL certificate expiry tracking
- Real-time dashboard updates via WebSocket
- Email alerts on incident open
- Public status page endpoint (no auth required)
- Full API documentation via Swagger UI

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL database (or Supabase)

### Configuration

Copy the example properties file and fill in your values:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

```properties
spring.datasource.url=jdbc:postgresql://HOST:5432/postgres
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

jwt.secret=YOUR_JWT_SECRET
jwt.expiration=86400000

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL@gmail.com
spring.mail.password=YOUR_APP_PASSWORD

notification.email.to=YOUR_EMAIL@gmail.com
```

### Run locally

```bash
./mvnw spring-boot:run
```

### Run with Docker

```bash
docker-compose up --build
```

## API Endpoints

Full documentation available at `http://localhost:8080/swagger-ui/index.html` once the server is running.

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | No | Register user |
| POST | `/api/auth/login` | No | Login, returns JWT |
| GET | `/api/projects` | Yes | List projects |
| POST | `/api/projects` | Yes | Create project |
| GET | `/api/projects/{id}/services` | Yes | List services |
| POST | `/api/projects/{id}/services` | Yes | Create service |
| GET | `/api/services/{id}/metrics` | Yes | Get service metrics |
| GET | `/api/services/{id}/incidents` | Yes | Get incidents |
| GET | `/public/status/{projectId}` | No | Public status page |

## Project Structure

```
src/main/java/com/pulse/
├── auth/          # JWT authentication
├── config/        # Security, CORS config
├── incident/      # Incident detection and management
├── metrics/       # Uptime, latency, p95 metrics
├── monitor/       # HTTP checker and scheduler
├── notification/  # Email alerts
├── project/       # Project CRUD
├── publicstatus/  # Public status endpoint
├── service/       # Monitored service CRUD
├── ssl/           # SSL certificate checker
└── websocket/     # WebSocket config and publisher
```

## Database Tables

| Table | Description |
|-------|-------------|
| `users` | Registered users |
| `projects` | Monitoring projects |
| `monitored_services` | Services being monitored |
| `monitor_checks` | HTTP check results |
| `incidents` | Detected incidents |
| `ssl_checks` | SSL certificate checks |

## Frontend

The frontend repository is available at [pulse-frontend](https://github.com/Pau-Balsach/pulse-frontend).
