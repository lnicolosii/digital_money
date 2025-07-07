# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Architecture Overview

Digital Money is a Spring Boot microservices application using Netflix OSS components for service discovery and API gateway functionality. The architecture consists of:

### Services Structure
- **eureka-server** (Port 8761): Service discovery registry
- **gateway** (Port 3500): API gateway with JWT authentication, circuit breaker, and routing
- **auth-service** (Port 8081): Authentication and JWT token management
- **user-service** (Port 8082): User management with MySQL integration
- **account-service** (Port 8083): Account, card, and transaction management with MySQL

### Service Communication
- All services register with Eureka Server for discovery
- Gateway routes requests using `lb://service-name` pattern
- Inter-service communication via OpenFeign clients
- JWT authentication handled at gateway level
- Circuit breaker pattern implemented with Resilience4j

## Development Commands

### Building Services
```bash
# Build individual service
mvn clean package

# Build with specific profile
mvn clean package -Pdev
mvn clean package -Pprod

# Build all services (from project root)
docker-compose build
```

### Running Services

#### Local Development
```bash
# Start infrastructure first
docker-compose up db eureka-server

# Run individual service locally
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Or with specific port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
```

#### Docker Environment
```bash
# Start all services
docker-compose up

# Start specific services
docker-compose up eureka-server gateway user-service

# Rebuild and start
docker-compose up --build
```

### Testing
```bash
# Run tests for a service
mvn test

# Run tests with specific profile
mvn test -Pdev
```

### Service URLs
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:3500
- Auth Service: http://localhost:8081
- User Service: http://localhost:8082
- Account Service: http://localhost:8083

## Key Technologies

### Core Stack
- **Java 17** with Spring Boot 3.4.5/3.5.0
- **Spring Cloud 2024.0.1/2025.0.0** for microservices
- **Maven** for build management
- **MySQL 8** for persistence

### Microservices Components
- **Netflix Eureka** for service discovery
- **Spring Cloud Gateway** with WebFlux for API gateway
- **OpenFeign** for service-to-service communication
- **Resilience4j** for circuit breaker pattern
- **JWT (JJWT)** for authentication

## Database Configuration

### Shared Database Setup
- Database name: `digital_money`
- Connection: `jdbc:mysql://db:3306/digital_money` (Docker) or `jdbc:mysql://localhost:3306/digital_money` (local)
- Credentials: root/rootpassword
- Hibernate DDL mode: `create-drop` in dev, `validate` in prod

### Services Using Database
- user-service: User profiles and data
- account-service: Accounts, cards, transactions with initial data loaded via import.sql

## Configuration Profiles

### Development (dev) - Default
- SQL logging enabled
- Hibernate shows SQL
- create-drop DDL mode
- Debug logging for Spring Cloud components

### Production (prod)
- Minimal logging
- validate DDL mode
- Optimized for performance

## Gateway Routing Configuration

The gateway routes requests as follows:
- `/users/**` → user-service
- `/accounts/**`, `/cards/**` → account-service
- `/auth/register`, `/auth/login`, `/auth/validate-token` → auth-service

## Security Implementation

### JWT Flow
1. User authenticates via auth-service (`/auth/login`)
2. JWT token returned and must be included in subsequent requests
3. Gateway validates JWT tokens using auth-service (`/auth/validate-token`)
4. Validated requests forwarded to appropriate microservices

### CORS Configuration
Gateway configured to allow all origins for development. Adjust for production deployment.

## Important Development Notes

### Service Dependencies
- Always start Eureka Server first
- Database must be running for user-service and account-service
- Gateway depends on Eureka Server
- Services take time to register with Eureka (allow 1-2 minutes)

### Inter-Service Communication
- Use service names (e.g., `user-service`, `account-service`) in Feign clients
- Circuit breaker automatically activated on failures
- Load balancing handled by Spring Cloud LoadBalancer

### Exception Handling
- Custom exception classes in each service for consistent error responses
- Global exception handlers using `@ControllerAdvice`
- Standard HTTP status codes and error response format

## Build Artifacts
- JAR files built with custom names (e.g., eureka.jar)
- Docker images created per service
- Maven profiles control build-time properties