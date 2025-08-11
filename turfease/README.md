# TurfEase - Microservices Sports Turf Booking Platform

This repository contains a complete microservices-based Java Fullstack application called TurfEase. It lets users browse and book sports turfs (football, cricket, badminton, etc.) and allows admins to manage turfs and bookings.

## Architecture
- Eureka Server (Service Discovery)
- API Gateway (Routing)
- Authentication Service (JWT, Role-based: USER/ADMIN)
- Turf Service (Turf CRUD)
- Booking Service (Create/Update/Cancel bookings, conflict checks)
- Payment Service (Simulated payment, updates booking status)
- Notification Service (Email confirmations; logs if SMTP not configured)
- React Frontend (User UI + Admin Dashboard)

Each service is an independent Spring Boot app (Java 17). All services register with Eureka and are accessed by the frontend via the API Gateway.

## Prerequisites
- Java 17 (JDK 17)
- Maven 3.9+
- Node.js 18+ and npm 9+
- MySQL 8+

## Quick Start
1. Create MySQL database:
   - Create a database named `turfease`.
   - Note your MySQL username and password.

2. Update database credentials and JWT secret in each service's `src/main/resources/application.properties`:
   - `spring.datasource.url=jdbc:mysql://localhost:3306/turfease`
   - `spring.datasource.username=YOUR_USERNAME`
   - `spring.datasource.password=YOUR_PASSWORD`
   - `jwt.secret=CHANGE_ME_TO_A_LONG_RANDOM_SECRET`

   Alternatively set environment variables:
   - `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`.

3. Start services in this order (new terminals):
   - Eureka Server
   - API Gateway
   - Authentication Service
   - Turf Service
   - Booking Service
   - Payment Service
   - Notification Service

4. Start the frontend:
   - `cd frontend`
   - `npm install`
   - `npm start`

5. Open the app at `http://localhost:3000`.

6. Import the Postman collection in `postman/TurfEase.postman_collection.json` and test APIs via `http://localhost:8080` (API Gateway).

## Ports
- Eureka Server: 8761
- API Gateway: 8080
- Authentication Service: 9001
- Turf Service: 9002
- Booking Service: 9003
- Payment Service: 9004
- Notification Service: 9005
- Frontend: 3000

## Default Users (seeded)
- Admin: `admin@turfease.com` / `Admin@1234`
- User: `user@turfease.com` / `User@1234`

These are inserted at first startup by the Authentication Service if they do not exist.

## Data Seeding
- Turf Service seeds a few sample turfs at startup.

## CORS
CORS is enabled for `http://localhost:3000` on all services.

## Email (Notification Service)
- By default, email notifications are logged to console.
- To enable SMTP, set in `notification-service/src/main/resources/application.properties`:
  - `spring.mail.host`, `spring.mail.port`, `spring.mail.username`, `spring.mail.password`, `spring.mail.properties.mail.smtp.auth=true`, `spring.mail.properties.mail.smtp.starttls.enable=true`.

## Build & Run
From the project root:

- Build all services:
  - `mvn -q -DskipTests package -pl eureka-server,api-gateway,auth-service,turf-service,booking-service,payment-service,notification-service -am`

- Run services individually:
  - `cd eureka-server && mvn spring-boot:run`
  - `cd api-gateway && mvn spring-boot:run`
  - `cd auth-service && mvn spring-boot:run`
  - `cd turf-service && mvn spring-boot:run`
  - `cd booking-service && mvn spring-boot:run`
  - `cd payment-service && mvn spring-boot:run`
  - `cd notification-service && mvn spring-boot:run`

## Environment Variables (optional)
All services honor:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `INTERNAL_SERVICE_TOKEN` (for inter-service secured endpoints)

## Postman Collection
Import `postman/TurfEase.postman_collection.json` and set the `baseUrl` variable to `http://localhost:8080`.

## Notes
- Database schema is auto-created via JPA (`spring.jpa.hibernate.ddl-auto=update`).
- All services register with Eureka and are reachable through the API Gateway.
- Frontend talks only to the Gateway.