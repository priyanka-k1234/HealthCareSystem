```markdown
# Hybrid Healthcare Database System

## Project Overview

The **Hybrid Healthcare Database System** is a secure, scalable, and user-friendly healthcare management platform designed to handle diverse healthcare data efficiently. It integrates both SQL and NoSQL databases to manage structured data (such as user accounts, patient profiles, and appointments) and flexible, evolving data (such as medical histories and sensor readings).

This hybrid approach leverages PostgreSQL for reliable, transactional data storage and MongoDB for flexible document-based storage, providing a comprehensive solution for modern healthcare data management.

---

## Features

- **User Management:** Role-based access control with Admin, Doctor, and Patient roles.  
- **Patient Management:** Create, read, update, and delete (CRUD) operations for patient profiles with search functionality.  
- **Appointment Scheduling:** Patients and doctors can book and manage appointments with status tracking.  
- **Medical History:** Doctors can view detailed patient medical histories stored in MongoDB.  
- **Sensor Data Monitoring:** Real-time sensor data collection and display from medical devices.  
- **Dashboard & Reporting:** Interactive charts and statistics for appointments, patient demographics, and sensor trends.  
- **Security:** Password hashing with BCrypt, multi-factor authentication (MFA) support, and secure role-based authorization.

---

## Technologies Used

- **Backend:** Java, Spring Boot, Spring Data JPA, Spring Data MongoDB, Spring Security  
- **Databases:** PostgreSQL (SQL), MongoDB (NoSQL)  
- **Frontend:** Thymeleaf, Bootstrap 5  
- **Build Tool:** Maven  

---

## Getting Started

### Prerequisites

- Java 17 or higher  
- Maven 3.6+  
- PostgreSQL database (configured and running)  
- MongoDB database (configured and running)  

### Installation and Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/yourusername/hybrid-healthcare-system.git
   cd hybrid-healthcare-system
   ```

2. **Configure database connection properties:**

   Edit `src/main/resources/application.properties` to set your PostgreSQL and MongoDB connection details:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/healthcare_system
   spring.datasource.username=your_postgres_username
   spring.datasource.password=your_postgres_password

   spring.data.mongodb.uri=mongodb://localhost:27017/healthcare_system

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

   server.port=8081
   ```

3. **Ensure databases are running:**

   - Start PostgreSQL and create the database `healthcare_system` if it does not exist.  
   - Start MongoDB and ensure it is accessible.

4. **Build the project:**

   ```bash
   mvn clean install
   ```

5. **Run the application:**

   ```bash
   mvn spring-boot:run
   ```

6. **Access the application:**

   Open your browser and navigate to `http://localhost:8081`.

---

## Default User Credentials

Use the following default accounts to log in and explore the system:

| Role      | Username   | Password    | Description                      |
|-----------|------------|-------------|---------------------------------|
| Admin     | admin      | admin       | Full system access and user management |
| Doctor 1  | drsmith    | docpass     | Doctor with patient management privileges |
| Doctor 2  | drjones    | docpass2    | Another doctor user              |
| Patient 1 | johndoe    | patientpass | Patient user with appointment booking |
| Patient 2 | janeroe    | patientpass2| Another patient user             |
| Patient 3 | bobsmith   | patientpass3| Additional patient user          |

---

## Usage

- **Login:**  
  Use the credentials above to log in.  
- **User Roles:**  
  - **Admin:** Manage users and system settings.  
  - **Doctor:** Manage patients, appointments, and view medical histories.  
  - **Patient:** View personal information and book appointments.  
- **Patient Management:** Add, edit, and search patient records.  
- **Appointment Scheduling:** Book and manage appointments with status updates.  
- **Medical History & Sensor Data:** View detailed medical records and live sensor data.  
- **Dashboard:** Monitor system statistics and trends via interactive charts.

---

## Project Architecture

### Hybrid Database Approach

- **PostgreSQL:** Stores structured, transactional data such as users, patients, and appointments.  
- **MongoDB:** Stores flexible, semi-structured data such as medical histories and sensor readings.

### Application Layers

- **Controller Layer:** Handles HTTP requests and routes them to services.  
- **Service Layer:** Contains business logic and coordinates data access between SQL and NoSQL databases.  
- **Repository Layer:** Interfaces with PostgreSQL and MongoDB using Spring Data JPA and Spring Data MongoDB respectively.  
- **Security Layer:** Manages authentication, authorization, password hashing, and multi-factor authentication.

---

## Sample Data Initialization

On first run, the application populates both databases with realistic sample data, including:

- Multiple users with different roles.  
- Patient records with demographic details.  
- Scheduled appointments with various statuses.  
- Detailed medical histories with allergies, medications, and surgeries.  
- Sensor data from medical devices with timestamps and context.

---

## Security Features

- Passwords are securely hashed using BCrypt.  
- Multi-factor authentication (MFA) is supported via Google Authenticator.  
- Role-based access control restricts data and functionality access according to user roles.  
- Sensitive data is protected in compliance with healthcare regulations.


## Contributing

Contributions are welcome! Please fork the repository and submit pull requests for bug fixes, improvements, or new features.

---


*Thank you for exploring the Hybrid Healthcare Database System!*
```
