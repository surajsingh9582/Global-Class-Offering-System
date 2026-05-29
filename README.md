# 🎓 Global Class Offering Booking System

A backend service for a global live-learning platform where teachers conduct online classes for students across different countries and time zones.

---

## 📌 Project Overview

The Global Class Offering Booking System is a RESTful backend application built using Spring Boot that enables teachers to manage class offerings and schedules while allowing parents/students to book available seats.

The system supports:

* Multiple course offerings
* Session scheduling
* Seat booking management
* Concurrency-safe reservations
* Timezone-independent scheduling
* Pagination for large datasets
* Transaction-safe booking operations

The application is designed to scale under high concurrent booking traffic while maintaining data consistency and preventing overbooking.

---

## 🛠️ Tech Stack Used

| Component           | Technology                 |
| ------------------- |----------------------------|
| Language            | Java 21                    |
| Framework           | Spring Boot                |
| ORM                 | Spring Data JPA, Hibernate |
| Database            | MySQL 8                    |
| Build Tool          | Maven                      |
| API Testing         | Postman                    |
| Version Control     | Git & GitHub               |
| Concurrency Control | Optimistic Locking         |
| Time Handling       | Java Instant (UTC)         |

---

## ⚙️ Setup Instructions

### Prerequisites

* Java 21 or above
* Maven 4.x
* MySQL 8.x
* Git

### Clone the Repository

```bash
git clone https://github.com/<your-username>/class-booking-service.git

cd class-booking-service
```

### Create Database

```sql
CREATE DATABASE class_booking;
```

### Configure Application Properties

Update `src/main/resources/application.properties`

```properties
server.port=8080
server.servlet.context-path=/api/v1

spring.datasource.url=jdbc:mysql://localhost:3306/class_booking?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.sql.init.mode=always
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.data.web.page-serialization-mode=via_dto
```

---

## 🔐 Environment Variables Required

The application requires the following environment variables:

| Variable                   | Description             | Example                                   |
| -------------------------- | ----------------------- | ----------------------------------------- |
| SERVER_PORT                | Application Port        | 8080                                      |
| SPRING_DATASOURCE_URL      | Database Connection URL | jdbc:mysql://localhost:3306/class_booking |
| SPRING_DATASOURCE_USERNAME | Database Username       | root                                      |
| SPRING_DATASOURCE_PASSWORD | Database Password       | password                                  |

Example:

```bash
export SERVER_PORT=8080

export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/class_booking

export SPRING_DATASOURCE_USERNAME=root

export SPRING_DATASOURCE_PASSWORD=password
```

---

# 📚 API Documentation

## Teacher APIs

### 1. Add Sessions to an Offering

**Endpoint**

```http
POST /teacher/offerings/{offeringId}/sessions
```

**Request Body**

```json
[
  {
    "startTime": "2026-06-01T09:00:00Z",
    "endTime": "2026-06-01T10:30:00Z"
  }
]
```

**Response**

```json
{
  "id": 1,
  "capacity": 30,
  "bookedSeats": 0
}
```

---

### 2. Get Teacher Offerings

**Endpoint**

```http
GET /teacher/{teacherId}/offerings?page=0&size=10
```

**Sample Response**

```json
{
  "content": [
    {
      "id": 1,
      "courseTitle": "Introduction to Python Programming",
      "capacity": 30,
      "bookedSeats": 4
    }
  ]
}
```

---

## Parent APIs

### 3. View Available Offerings

**Endpoint**

```http
GET /parent/offerings/available?page=0&size=10
```

Returns all offerings where booked seats are less than capacity.

---

### 4. Create Booking

**Endpoint**

```http
POST /bookings
```

**Request Body**

```json
{
  "parentId": 1,
  "offeringId": 1
}
```

**Response**

```json
{
  "bookingId": 251,
  "offeringId": 1,
  "status": "CONFIRMED",
  "createdAt": "2026-05-29T22:15:30Z"
}
```

---

### 5. Booking History

**Endpoint**

```http
GET /parent/{parentId}/bookings?page=0&size=10
```

Returns all bookings associated with the specified parent.

---

# 🗄️ Database Schema Overview

## Entity Relationships

```text
Teacher
   |
   | 1:N
   |
Offering
   |
   | 1:N
   |
Session

Course
   |
   | 1:N
   |
Offering

Parent
   |
   | 1:N
   |
Booking
   |
   | N:1
   |
Offering
```

---

## Core Tables

### Courses

```sql
CREATE TABLE courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255)
);
```

### Teachers

```sql
CREATE TABLE teachers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255)
);
```

### Parents

```sql
CREATE TABLE parent (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255)
);
```

### Offerings

```sql
CREATE TABLE offering (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT,
    teacher BIGINT,
    capacity INT DEFAULT 30,
    booked_seats INT DEFAULT 0,
    version INT
);
```

### Sessions

```sql
CREATE TABLE session (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    offering_id BIGINT,
    start_time DATETIME(6),
    end_time DATETIME(6)
);
```

### Bookings

```sql
CREATE TABLE booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT,
    offering_id BIGINT,
    created_at DATETIME(6)
);
```

---

# 📝 Assumptions Made

### Default Capacity

If capacity is not explicitly provided during offering creation, the system automatically assigns:

```text
30 seats
```

### Parent Ownership

Parents act as the primary booking owners and create reservations on behalf of students.

### Session Dependency

A session cannot exist without an associated offering.

Deleting an offering automatically removes all linked sessions.

### One Booking = One Seat

Each booking reserves exactly one seat in an offering.

---

# 🔄 Concurrency Handling Approach

To prevent overbooking during simultaneous booking attempts, the system uses **Optimistic Locking**.

### Implementation

```java
@Version
private Integer version;
```

### Workflow

1. User requests booking.
2. System reads current offering data.
3. Seat availability is verified.
4. Booking is processed.
5. Hibernate validates version number before update.
6. If another transaction has already modified the row:

    * OptimisticLockException is thrown.
    * Transaction is rolled back.
    * User receives an appropriate error response.

### Benefits

* Prevents overbooking
* Ensures consistency
* Supports high concurrency
* Avoids unnecessary database locks

---

# 🌍 Timezone Handling Approach

All timestamps are stored in UTC using ISO-8601 format.

### Example

```text
2026-06-01T09:00:00Z
```

### Entity Fields

```java
Instant startTime;
Instant endTime;
Instant createdAt;
```

### Why UTC?

* Consistent across countries
* Eliminates daylight-saving issues
* Simplifies scheduling
* Easy frontend localization

---

# ▶️ Steps to Run the Application Locally

### Step 1: Start MySQL

```bash
mysql.server start
```

### Step 2: Create Database

```sql
CREATE DATABASE class_booking;
```

### Step 3: Configure Credentials

Update:

```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

### Step 4: Build Project

```bash
mvn clean install
```

### Step 5: Run Application

```bash
mvn spring-boot:run
```

### Step 6: Verify Application

```http
GET http://localhost:8080/api/v1
```

### Step 7: Import Postman Collection

Import:

```text
ClassBooking_Postman_Collection.json
```

and test all APIs.

---

# 📂 Project Structure

```text
src
├── main
│   ├── java
│   │   ├── controller
│   │   ├── service
│   │   ├── repository
│   │   ├── entity
│   │   ├── dto
│   │   └── config
│   └── resources
│       ├── application.properties
│       └── schema.sql
└── test
```

---

# 🚀 Future Improvements

* JWT Authentication & Authorization
* Role-Based Access Control (Teacher/Parent/Admin)
* Redis Caching
* Kafka Event Streaming
* Notification Service (Email/SMS)
* Waitlist Management
* Distributed Locking for Multi-Node Deployment
* Docker & Kubernetes Deployment

---

# 👨‍💻 Author

**Suraj Kumar Singh**

Software Engineer | Java Backend Developer

### Skills

* Java
* Spring Boot
* Hibernate
* JPA
* MySQL
* REST APIs
* Docker
* Kafka
* Redis

GitHub:
https://github.com/surajsingh9582

LinkedIn:
https://www.linkedin.com/in/suraj-singh-7624a6293/
