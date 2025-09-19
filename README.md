# E-commerce-Backend-Amazon-Like

A comprehensive microservices-based E-Commerce Backend API built with Java Spring Boot, featuring modern architecture patterns and cloud-native technologies.

## 🔧 Tech Stack

- **Backend**: Java 17 + Spring Boot 3.1.4
- **Database**: PostgreSQL with Flyway migrations
- **Cache**: Redis
- **Message Queue**: Apache Kafka
- **Authentication**: JWT with Spring Security
- **API Documentation**: OpenAPI 3 (Swagger)
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven

## 📌 Features

### ✅ Completed
- **Project Infrastructure**: Maven setup, Docker configuration, application structure
- **Database Design**: Complete PostgreSQL schema with migrations for users, products, orders, inventory, and cart
- **User Management Service**: 
  - User registration and authentication (JWT)
  - Profile management
  - Address management
  - Role-based access control (Customer/Admin)

### 🚧 In Progress
- Product catalog with search and filters
- Cart service with stock locking
- Order service with payment simulation
- Inventory service with async updates via Kafka
- Admin dashboard: order status, inventory, user data

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Maven 3.6+

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd E-Commerce-Backend-API
   ```

2. **Start the infrastructure services**
   ```bash
   docker-compose up -d postgres redis kafka
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the API documentation**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/api-docs

## 📚 API Endpoints

### Authentication
- `POST /auth/signup` - Register new user
- `POST /auth/signin` - User login

### User Management
- `GET /users/profile` - Get current user profile
- `PUT /users/profile` - Update user profile
- `GET /users/addresses` - Get user addresses
- `POST /users/addresses` - Add new address
- `DELETE /users/addresses/{id}` - Delete address

## 🏗️ Architecture

This project follows a modular monolithic architecture with clear separation of concerns:

- **Entity Layer**: JPA entities with audit capabilities
- **Repository Layer**: Spring Data JPA repositories
- **Service Layer**: Business logic implementation
- **Controller Layer**: REST API endpoints
- **Security Layer**: JWT-based authentication and authorization
- **Exception Handling**: Global exception handler with custom responses

## 💼 Skills Demonstrated

- **Modern Spring Boot**: Latest version with Java 17 features
- **Database Design**: Comprehensive schema with proper relationships and constraints
- **Security**: JWT authentication with role-based access control
- **API Design**: RESTful APIs with OpenAPI documentation
- **Data Validation**: Bean validation with custom error responses
- **Docker**: Containerized application with multi-service setup
- **Database Migrations**: Flyway for version-controlled schema changes

## 🗄️ Database Schema

The application uses a well-designed PostgreSQL schema with the following main entities:
- **Users & Addresses**: User management with multiple addresses
- **Categories & Brands**: Product organization
- **Products**: Complete product catalog with attributes and images
- **Inventory**: Stock management with movement tracking
- **Shopping Carts**: Persistent cart with expiration
- **Orders**: Complete order management with status tracking
- **Payments**: Payment processing simulation

## 🔄 Next Steps

1. Product Catalog Service implementation
2. Inventory Management with Kafka integration
3. Shopping Cart functionality
4. Order processing pipeline
5. Admin dashboard APIs
6. Comprehensive testing suite
7. Kubernetes deployment configurationd-Amazon-Like

### 🔧 Tech Stack:
	-	````Java + Spring Boot, PostgreSQL, Redis
	-	gRPC, Kafka, Docker/K8s
	-	Optional: React frontend

### 📌 Features:
	-	Product catalog with search and filters
	-	Cart service with stock locking
	-	Order service with payment simulation
	-	Inventory service with async update via Kafka
	-	Admin dashboard: order status, inventory, user data

### 💼 Skills Demonstrated:
	-	Microservices + eventual consistency (Kafka)
	-	CQRS/DDD-style modeling
	-	Strong schema design and DB optimization
	-	Transactional vs async operations understanding
