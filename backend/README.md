# Rentit Backend API

Backend API for the Rentit house rental platform built with Spring Boot, Spring Security, and JPA/Hibernate.

## Features

- User authentication with JWT tokens
- Role-based access control (Tenant, Owner, Admin)
- Property management (CRUD operations)
- Advanced search with filters
- Booking system with approval workflow
- Review and rating system
- Image upload and management
- Admin dashboard with analytics
- API documentation with Swagger/OpenAPI

## Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MySQL (with H2 for testing)
- **Security**: Spring Security with JWT
- **ORM**: Spring Data JPA/Hibernate
- **Documentation**: Springdoc OpenAPI (Swagger)
- **Build Tool**: Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0 or higher

## Installation

1. Navigate to the backend directory:
```bash
cd backend
```

2. Create a MySQL database:
```sql
CREATE DATABASE rentit;
```

3. Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rentit
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. Update JWT secrets (important for production):
```properties
jwt.secret=your_secure_jwt_secret_minimum_256_bits
jwt.refresh-secret=your_secure_refresh_secret
```

5. Install dependencies:
```bash
mvn clean install
```

## Running the Application

### Development mode:
```bash
mvn spring-boot:run
```

### Build for production:
```bash
mvn clean package
java -jar target/rentit-backend-1.0.0.jar
```

The server will start on `http://localhost:5000`

## API Documentation

Once the server is running, access the Swagger UI at:
```
http://localhost:5000/swagger-ui.html
```

API docs JSON:
```
http://localhost:5000/v3/api-docs
```

## API Endpoints

### Authentication (`/api/auth`)
- `POST /register` - Register a new user
- `POST /login` - Login user
- `POST /refresh` - Refresh access token
- `POST /logout` - Logout user
- `GET /profile` - Get user profile
- `POST /reset-password-request` - Request password reset
- `POST /reset-password` - Reset password

### Properties (`/api/properties`)
- `POST /` - Create property (Owner/Admin)
- `GET /` - Get all properties (with pagination)
- `GET /{id}` - Get property by ID
- `PUT /{id}` - Update property (Owner/Admin)
- `DELETE /{id}` - Delete property (Owner/Admin)
- `GET /owner/{ownerId}` - Get owner's properties
- `PATCH /{id}/status` - Update property status

### Search (`/api/search`)
- `GET /` - Search properties with filters
- `GET /nearby` - Search nearby properties

### Bookings (`/api/bookings`)
- `POST /` - Create booking (Tenant)
- `GET /{id}` - Get booking details
- `PATCH /{id}/approve` - Approve booking (Owner)
- `PATCH /{id}/reject` - Reject booking (Owner)
- `PATCH /{id}/cancel` - Cancel booking (Tenant)
- `GET /tenant/{tenantId}` - Get tenant's bookings
- `GET /owner/{ownerId}` - Get owner's bookings

### Reviews (`/api/reviews`)
- `POST /` - Submit review (Tenant)
- `GET /property/{propertyId}` - Get property reviews
- `PUT /{id}` - Update review
- `DELETE /{id}` - Delete review

### Images (`/api/images`)
- `POST /upload` - Upload images
- `GET /{filename}` - Get image
- `DELETE /{filename}` - Delete image

### Admin (`/api/admin`)
- `GET /users` - Get all users
- `DELETE /users/{id}` - Delete user
- `PATCH /users/{id}/role` - Update user role
- `GET /properties/pending` - Get pending properties
- `PATCH /properties/{id}/verify` - Verify property
- `GET /analytics` - Get platform analytics

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/rentit/
│   │   │   ├── config/           # Configuration classes
│   │   │   │   ├── AppConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── SwaggerConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/       # REST controllers
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── BookingController.java
│   │   │   │   ├── ImageController.java
│   │   │   │   ├── PropertyController.java
│   │   │   │   ├── ReviewController.java
│   │   │   │   └── SearchController.java
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── BookingDTO.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── PropertyDTO.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   └── ...
│   │   │   ├── entity/           # JPA entities
│   │   │   │   ├── Booking.java
│   │   │   │   ├── Property.java
│   │   │   │   ├── PropertyImage.java
│   │   │   │   ├── Review.java
│   │   │   │   └── User.java
│   │   │   ├── exception/        # Exception handling
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── repository/       # JPA repositories
│   │   │   │   ├── BookingRepository.java
│   │   │   │   ├── PropertyRepository.java
│   │   │   │   ├── ReviewRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── security/         # Security components
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── JwtUtil.java
│   │   │   ├── service/          # Business logic
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── BookingService.java
│   │   │   │   ├── PropertyService.java
│   │   │   │   └── ReviewService.java
│   │   │   └── RentitApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application.properties.example
│   └── test/java/com/rentit/    # Test classes
├── pom.xml
└── README.md
```

## Database Schema

### Users Table
- `id` (Primary Key)
- `username` (Unique)
- `email` (Unique)
- `password` (Encrypted)
- `role` (TENANT/OWNER/ADMIN)
- `phone`
- `refresh_token`
- `reset_password_token`
- `reset_password_expire`
- `created_at`
- `updated_at`

### Properties Table
- `id` (Primary Key)
- `owner_id` (Foreign Key → Users)
- `title`
- `description`
- `property_type` (APARTMENT/HOUSE/VILLA/STUDIO/ROOM)
- `rent_amount`
- `deposit`
- `address`
- `city`
- `state`
- `pincode`
- `latitude`
- `longitude`
- `bedrooms`
- `bathrooms`
- `area_sqft`
- `availability_status` (AVAILABLE/RENTED/DRAFT)
- `is_verified`
- `views`
- `deleted_at`
- `created_at`
- `updated_at`

### Property_Amenities Table
- `property_id` (Foreign Key → Properties)
- `amenity` (e.g., "WiFi", "Parking")

### Property_Images Table
- `id` (Primary Key)
- `property_id` (Foreign Key → Properties)
- `url`
- `is_primary`
- `created_at`

### Bookings Table
- `id` (Primary Key)
- `property_id` (Foreign Key → Properties)
- `tenant_id` (Foreign Key → Users)
- `owner_id` (Foreign Key → Users)
- `booking_status` (PENDING/APPROVED/REJECTED/CANCELLED)
- `check_in_date`
- `booking_date`
- `notes`
- `created_at`
- `updated_at`

### Reviews Table
- `id` (Primary Key)
- `property_id` (Foreign Key → Properties)
- `tenant_id` (Foreign Key → Users)
- `rating` (1-5)
- `comment`
- `created_at`
- `updated_at`

## Security Features

- Password hashing with BCrypt
- JWT-based authentication with refresh tokens
- Role-based access control (RBAC) with Spring Security
- Method-level security with `@PreAuthorize`
- Input validation with Bean Validation
- Global exception handling
- CORS configuration
- Stateless session management

## Configuration

### Application Properties

Key configuration properties in `application.properties`:

```properties
# Server
server.port=5000

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/rentit
spring.datasource.username=root
spring.datasource.password=password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=your_secret_key
jwt.expiration=3600000
jwt.refresh-secret=your_refresh_secret
jwt.refresh-expiration=604800000

# File Upload
spring.servlet.multipart.max-file-size=5MB
file.upload-dir=./uploads

# CORS
cors.allowed-origins=http://localhost:3000
```

## Testing

Run tests:
```bash
mvn test
```

Run with coverage:
```bash
mvn test jacoco:report
```

## Deployment

### Using JAR:
```bash
mvn clean package
java -jar target/rentit-backend-1.0.0.jar
```

### Using Docker:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/rentit-backend-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## Environment Variables

For production, use environment variables instead of application.properties:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/rentit
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=password
export JWT_SECRET=your_production_secret
export JWT_REFRESH_SECRET=your_production_refresh_secret
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

MIT
