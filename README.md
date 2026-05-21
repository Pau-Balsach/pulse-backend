# Pulse — Backend

![Java](https://img.shields.io/badge/Java-21-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Supabase-blue) ![Docker](https://img.shields.io/badge/Docker-ready-blue)

**API REST:** https://pulse-backend-kurk.onrender.com  
**Swagger UI:** https://pulse-backend-kurk.onrender.com/swagger-ui/index.html  
**Frontend:** https://github.com/Pau-Balsach/pulse-frontend

---

## ¿Qué es Pulse?

Pulse es una plataforma de observabilidad SaaS similar a UptimeRobot o BetterStack. Permite monitorizar servicios web en tiempo real, detectar caídas automáticamente, revisar el historial de incidencias y consultar el estado de todos los servicios desde una página pública sin necesidad de autenticación.

Este repositorio contiene el **backend**, encargado de:

- Autenticar usuarios mediante JWT
- Gestionar proyectos y servicios monitorizados
- Lanzar comprobaciones HTTP automáticas cada 60 segundos
- Detectar y resolver incidencias automáticamente
- Comprobar la validez y caducidad de certificados SSL
- Enviar alertas por email cuando se detecta una incidencia
- Emitir actualizaciones en tiempo real al dashboard mediante WebSockets
- Exponer una página de estado pública por proyecto

---

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje principal |
| Spring Boot 3.5 | Framework backend |
| Spring Security + JWT | Autenticación stateless |
| Spring Data JPA | Acceso a base de datos |
| PostgreSQL (Supabase) | Base de datos en la nube |
| WebSockets (STOMP) | Actualizaciones en tiempo real |
| JavaMailSender (SMTP) | Notificaciones por email |
| Springdoc OpenAPI | Documentación automática de la API |
| Docker | Containerización |

---

## Estructura del código

```
src/main/java/com/pulse/
├── auth/               # Registro, login y filtros JWT
├── config/             # SecurityConfig, CORS, UserDetailsService
├── incident/           # Detección y gestión de incidencias
├── metrics/            # Uptime %, latencia media, p95, total checks
├── monitor/            # HttpChecker y MonitorScheduler (@Scheduled)
├── notification/       # Envío de emails con JavaMailSender
├── project/            # CRUD de proyectos
├── publicstatus/       # Endpoint público /public/status/{projectId}
├── service/            # CRUD de servicios monitorizados
├── ssl/                # SslChecker, entidad y repositorio
└── websocket/          # WebSocketConfig y MonitorWebSocketPublisher
```

---

## Base de datos

| Tabla | Descripción |
|---|---|
| `users` | Usuarios registrados |
| `projects` | Proyectos de monitorización |
| `monitored_services` | Servicios configurados para monitorizar |
| `monitor_checks` | Resultados de cada comprobación HTTP |
| `incidents` | Incidencias detectadas (OPEN / RESOLVED) |
| `ssl_checks` | Resultados de comprobaciones de certificado SSL |

---

## Endpoints principales

| Método | Ruta | Auth | Descripción |
|---|---|---|---|
| POST | `/api/auth/register` | No | Registrar usuario |
| POST | `/api/auth/login` | No | Login, devuelve JWT |
| GET | `/api/projects` | Sí | Listar proyectos del usuario |
| POST | `/api/projects` | Sí | Crear proyecto |
| DELETE | `/api/projects/{id}` | Sí | Eliminar proyecto |
| GET | `/api/projects/{id}/services` | Sí | Listar servicios de un proyecto |
| POST | `/api/projects/{id}/services` | Sí | Crear servicio |
| DELETE | `/api/services/{id}` | Sí | Eliminar servicio |
| GET | `/api/services/{id}/metrics` | Sí | Métricas del servicio (uptime, latencia, p95) |
| GET | `/api/services/{id}/incidents` | Sí | Historial de incidencias |
| GET | `/public/status/{projectId}` | No | Página de estado pública del proyecto |

Documentación completa disponible en [Swagger UI](https://pulse-backend-kurk.onrender.com/swagger-ui/index.html).

---

## Configuración

Copia el archivo de ejemplo y rellena los valores:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

### application.properties.example

```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://HOST:5432/postgres
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Puerto
server.port=8080

# JWT
jwt.secret=YOUR_JWT_SECRET_KEY
jwt.expiration=86400000

# Mail (Gmail con App Password)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL@gmail.com
spring.mail.password=YOUR_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Notificaciones
notification.email.to=YOUR_EMAIL@gmail.com
```

---

## Ejecución local

```bash
# Con Maven
./mvnw spring-boot:run

# Con Docker (junto al frontend)
docker-compose up --build
```

---

## Frontend

El repositorio del frontend está disponible en [pulse-frontend](https://github.com/Pau-Balsach/pulse-frontend).