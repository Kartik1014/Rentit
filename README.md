# Rentit - House Rental Platform

A full-stack house rental platform that connects property owners with tenants. Built with Spring Boot (Java) backend and React frontend.

## Overview

Rentit is a comprehensive property rental platform that enables:
- **Tenants** to search and book rental properties
- **Property Owners** to list and manage their properties
- **Admins** to oversee platform operations and verify properties

## Features

### For Tenants
- Browse and search properties with advanced filters
- View detailed property information with image galleries
- Book properties and track booking status
- Submit reviews and ratings for properties
- Manage booking history

### For Property Owners
- List properties with detailed information and images
- Manage property listings (add, edit, delete)
- Review and approve/reject booking requests
- Track property views and bookings
- Update property availability status

### For Administrators
- Verify and approve property listings
- Manage users and assign roles
- View platform analytics and statistics
- Moderate reviews and content

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MySQL (with H2 for testing)
- **Security**: Spring Security + JWT
- **ORM**: Spring Data JPA / Hibernate
- **Documentation**: Springdoc OpenAPI (Swagger)
- **Build Tool**: Maven

### Frontend
- **Framework**: React 18
- **Build Tool**: Vite
- **Styling**: Tailwind CSS
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **State Management**: Context API

## Project Structure

```
Rentit/
├── backend/                 # Spring Boot backend
│   ├── src/
│   │   ├── main/java/com/rentit/
│   │   │   ├── config/     # Configuration classes
│   │   │   ├── controller/ # REST controllers
│   │   │   ├── dto/        # Data Transfer Objects
│   │   │   ├── entity/     # JPA entities
│   │   │   ├── exception/  # Exception handling
│   │   │   ├── repository/ # JPA repositories
│   │   │   ├── security/   # Security components
│   │   │   ├── service/    # Business logic
│   │   │   └── RentitApplication.java
│   │   └── resources/
│   │       └── application.properties
│   ├── pom.xml
│   └── README.md
│
├── frontend/                # React frontend
│   ├── src/
│   │   ├── components/     # Reusable components
│   │   ├── context/        # React Context
│   │   ├── pages/          # Page components
│   │   ├── services/       # API services
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── package.json
│   └── README.md
│
└── README.md               # This file
```

## Quick Start

### Prerequisites
- Java 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Create MySQL database:
```sql
CREATE DATABASE rentit;
```

3. Update `src/main/resources/application.properties` with your database credentials.

4. Run the application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:5000`

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Create `.env` file from `.env.example` and configure API URL.

4. Start the development server:
```bash
npm run dev
```

The frontend will start on `http://localhost:3000`

## API Documentation

Once the backend is running, access the Swagger UI at:
```
http://localhost:5000/swagger-ui.html
```

## Main API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Refresh JWT token
- `GET /api/auth/profile` - Get user profile

### Properties
- `GET /api/properties` - List all properties
- `POST /api/properties` - Create property (Owner/Admin)
- `GET /api/properties/{id}` - Get property details
- `PUT /api/properties/{id}` - Update property (Owner/Admin)
- `DELETE /api/properties/{id}` - Delete property (Owner/Admin)

### Search
- `GET /api/search` - Search properties with filters
- `GET /api/search/nearby` - Search nearby properties

### Bookings
- `POST /api/bookings` - Create booking (Tenant)
- `PATCH /api/bookings/{id}/approve` - Approve booking (Owner)
- `PATCH /api/bookings/{id}/reject` - Reject booking (Owner)
- `PATCH /api/bookings/{id}/cancel` - Cancel booking (Tenant)

### Reviews
- `POST /api/reviews` - Submit review (Tenant)
- `GET /api/reviews/property/{propertyId}` - Get property reviews

### Admin
- `GET /api/admin/users` - List all users
- `GET /api/admin/properties/pending` - Get pending property approvals
- `PATCH /api/admin/properties/{id}/verify` - Verify property
- `GET /api/admin/analytics` - Get platform analytics

## Database Schema

### Users
- Authentication and profile information
- Roles: TENANT, OWNER, ADMIN

### Properties
- Property listings with details
- Images, amenities, location
- Status: AVAILABLE, RENTED, DRAFT

### Bookings
- Booking requests and approvals
- Status: PENDING, APPROVED, REJECTED, CANCELLED

### Reviews
- Property reviews and ratings
- One review per tenant per property

## Security Features

- JWT-based authentication with refresh tokens
- Password hashing with BCrypt
- Role-based access control (RBAC)
- CORS configuration
- Input validation
- SQL injection prevention
- XSS protection

## Development

### Backend
```bash
cd backend
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm run dev
```

### Testing

Backend:
```bash
cd backend
mvn test
```

Frontend:
```bash
cd frontend
npm test
```

## Deployment

### Backend
```bash
cd backend
mvn clean package
java -jar target/rentit-backend-1.0.0.jar
```

### Frontend
```bash
cd frontend
npm run build
# Deploy the dist/ folder to your hosting service
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please open an issue in the GitHub repository.

## Acknowledgments

- Spring Boot for the robust backend framework
- React for the dynamic frontend
- Tailwind CSS for beautiful styling
- All contributors and supporters of this project